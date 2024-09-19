CREATE OR REPLACE PROCEDURE drogueria_schema.sp_buy_transaction(
    IN p_user_id bigint,
    OUT out_message character varying,
    OUT out_status boolean,
    OUT out_transaction_id BIGINT,
    OUT out_total double precision)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
    -- VARIABLES DE CARTS
    v_cart_id BIGINT; -- VARIABLE PARA ALMACENAR EL ID DEL CARRITO

    -- VARIABLES DE CART_ITEM
    v_cart_item_id BIGINT; -- VARIABLE PARA ALMACENAR EL ID DEL ITEM EN EL CARRITO
    v_product_cart_item_id BIGINT; -- VARIABLE PARA ALMACENAR EL ID DEL PRODUCTO EN EL CARRITO
    v_cart_item_quantity INT; -- VARIABLE PARA ALMACENAR LA CANTIDAD DE UN PRODUCTO EN EL CARRITO

    -- VARIABLES DE PRODUCTS
    v_product_id BIGINT;
    v_product_stock INT;
    v_product_amount DOUBLE PRECISION;

    -- VARIABLES DE TRANSACTIONS
    v_transaction_id BIGINT;

BEGIN
    -- Obtener el ID del carrito para el usuario dado
    SELECT id INTO v_cart_id
    FROM drogueria_schema.carts
    WHERE user_id = p_user_id;

    -- Inicializar los parámetros de salida
    out_status := FALSE;
    out_transaction_id := NULL;
    out_total := 0;

    -- Validar si el carrito existe para el usuario
    IF v_cart_id IS NULL
    THEN
        out_message := 'Cart not found for user';
        INSERT INTO drogueria_schema.sp_error_logs(procedure_name, message_error, error_date)
        VALUES('sp_buy_transaction', out_message, TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'));
    END IF;

    -- Insertar la transacción primero
    INSERT INTO drogueria_schema.transactions (date_insert_trx, date_update_trx, reference, type_trx)
    VALUES (
        TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'),
        TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'),
        LPAD(FLOOR(RANDOM() * 1000000)::INT::TEXT, 6, '0'),
        'SALE'
    ) RETURNING id INTO v_transaction_id;

    -- Iterar sobre los items del carrito
    FOR v_cart_item_id, v_product_cart_item_id, v_cart_item_quantity IN
        SELECT id, product_id, quantity
        FROM drogueria_schema.cart_item
        WHERE cart_id = v_cart_id
    LOOP
        -- Obtener el producto y su stock
        SELECT id, stock, amount INTO v_product_id, v_product_stock, v_product_amount
        FROM drogueria_schema.products
        WHERE id = v_product_cart_item_id;

        -- Validar stock del producto
        IF v_product_stock > 0
        THEN
            -- Verificar que hay suficiente stock para cubrir la cantidad del carrito
            IF v_product_stock > v_cart_item_quantity
            THEN
                -- Actualizar el stock
                UPDATE drogueria_schema.products
                SET stock = (v_product_stock - v_cart_item_quantity)
                WHERE id = v_product_cart_item_id;

                -- Calcular el total para este producto y añadirlo al total general
                out_total := out_total + (v_cart_item_quantity * v_product_amount);

                -- Insertar en la tabla de relación de productos con la transacción
                INSERT INTO drogueria_schema.transaction_products(transaction_id, product_id)
                VALUES (v_transaction_id, v_product_id);

				-- Inserta en la tabla de transaction_items los productos usados en la transaccion para manternerlos
				INSERT INTO drogueria_schema.transaction_item(transaction_id, product_id, quantity)
				VALUES (v_transaction_id, v_product_id, v_cart_item_quantity);

                -- Eliminar el producto del carrito
                DELETE FROM drogueria_schema.cart_item
                WHERE id = v_cart_item_id;

				out_status := TRUE;
            ELSE
                -- Si no hay suficiente stock
                out_message := 'The stock of the product is less than the quantity in the cart';
                INSERT INTO drogueria_schema.sp_error_logs(procedure_name, message_error, error_date)
                VALUES('sp_buy_transaction', out_message, TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'));
            END IF;
        ELSE
            -- Si el producto no tiene stock disponible
            out_message := 'The product does not have stock available';
            INSERT INTO drogueria_schema.sp_error_logs(procedure_name, message_error, error_date)
            VALUES('sp_buy_transaction', out_message, TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'));
        END IF;
    END LOOP;

    -- Si ocurrió un error, eliminar la transacción
    IF out_status = FALSE AND v_transaction_id IS NOT NULL THEN
        DELETE FROM drogueria_schema.transactions WHERE id = v_transaction_id;
        out_transaction_id := NULL;
        out_message := 'The transaction failed and was rolled back';
    ELSE
        -- Si todo salió bien
        out_transaction_id := v_transaction_id;
        out_message := 'The transaction was processed successfully';
    END IF;

END;
$BODY$;
ALTER PROCEDURE drogueria_schema.sp_buy_transaction(bigint)
    OWNER TO postgres;
