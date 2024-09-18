CREATE OR REPLACE PROCEDURE drogueria_schema.sp_buy_transaction(IN p_user_id BIGINT)
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
	v_product_stock INT;

BEGIN
	SELECT id INTO v_cart_id
	FROM drogueria_schema.carts
	WHERE USER_id = p_user_id;

	IF v_cart_id IS NULL
	THEN
		RAISE EXCEPTION 'Carrito no encontrado para el usuario con ID: %', p_user_id;
	END IF;

	FOR v_cart_item_id, v_product_cart_item_id, v_cart_item_quantity IN
		SELECT id, product_id, quantity
		FROM drogueria_schema.cart_item
		WHERE cart_id = v_cart_id
	LOOP
		--RAISE NOTICE 'Procesando item con ID: %, Cantidad: %', v_product_cart_item_id, v_cart_item_quantity;
		SELECT stock INTO v_product_stock
		FROM drogueria_schema.products
		WHERE id = v_product_cart_item_id;

		UPDATE drogueria_schema.products
		SET stock = (v_product_stock - v_cart_item_quantity)
		WHERE id = v_product_cart_item_id;

		DELETE
		FROM drogueria_schema.cart_item
		WHERE id = v_cart_item_id;
	END LOOP;

	INSERT
	INTO drogueria_schema.transactions (date_insert_trx, date_update_trx, reference, type_trx)
	VALUES (
		TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'),
		TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'),
		LPAD(FLOOR(RANDOM() * 1000000)::INT::TEXT, 6, '0'),
		'SALE'
	);

END;
$$;