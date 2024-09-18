CREATE OR REPLACE PROCEDURE drogueria_schema.sp_buy_transaction(
	IN p_user_id BIGINT,
	OUT out_message VARCHAR,
	OUT out_status BOOLEAN
)
LANGUAGE plpgsql
AS $$
DECLARE
	-- VARIABLES DE CARTS
	v_cart_id BIGINT; -- VARIABLE PARA ALMACENAR EL ID DEL CARRITO

	-- VARIABLES DE CART_ITEM
	v_cart_item_id BIGINT; -- VARIABLE PARA ALMACENAR EL ID DEL
	v_product_cart_item_id BIGINT; -- VARIABLE PARA ALMACENAR EL ID DEL ITEM EN EL CARRITO
	v_cart_item_quantity INT; -- VARIABLE PARA ALAMACENAR LA CANTIDAD DE UN PRODUCTO EN EL CARRITO

	-- VARIABLES DE PRODUCTS
	v_product_id BIGINT;
	v_product_stock INT;

	-- VARIABLES DE TRANSACTIONS
	v_transaction_id BIGINT;

BEGIN
	BEGIN
		SELECT id INTO v_cart_id
		FROM drogueria_schema.carts
		WHERE USER_id = p_user_id;

		-- INICIALIZAMOS LA VARIABLE BOOLEANA EN TRUE
		out_status := TRUE;

		IF v_cart_id IS NULL
		THEN
			out_message := 'Cart not found for user';
			out_status := FALSE;
		END IF;

		FOR v_cart_item_id, v_product_cart_item_id, v_cart_item_quantity IN
			SELECT id, product_id, quantity
			FROM drogueria_schema.cart_item
			WHERE cart_id = v_cart_id
		LOOP
			SELECT id, stock INTO v_product_id, v_product_stock
			FROM drogueria_schema.products
			WHERE id = v_product_cart_item_id;

			IF v_product_stock > 0
			THEN
				IF v_product_stock > v_cart_item_quantity
				THEN
					-- ACTUALIZAMOS EL STOCK
					UPDATE drogueria_schema.products
					SET stock = (v_product_stock - v_cart_item_quantity)
					WHERE id = v_product_cart_item_id;

					-- ELIMINAMOS EL PRODUCTO AGREGADO AL CARRITO
					DELETE
					FROM drogueria_schema.cart_item
					WHERE id = v_cart_item_id;
				ELSE
					out_message := 'The stock of the product is less that the quantity of products entered into the cart';
					out_status := FALSE;
				END IF;
			ELSE
				out_message := 'The product does not have stock available';
				out_status := FALSE;
			END IF;
		END LOOP;

		IF out_status
		THEN
			INSERT
			INTO drogueria_schema.transactions (date_insert_trx, date_update_trx, reference, type_trx)
			VALUES (
				TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'),
				TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'),
				LPAD(FLOOR(RANDOM() * 1000000)::INT::TEXT, 6, '0'),
				'SALE'
			)RETURNING id INTO v_transaction_id;

			INSERT INTO drogueria_schema.transaction_products(transaction_id, product_id)
			VALUES (v_transaction_id, v_product_id);

			out_message := 'The transaction was processed successfully';
		END IF;
	END;

END;
$$;