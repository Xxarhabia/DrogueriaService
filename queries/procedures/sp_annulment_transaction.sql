CREATE OR REPLACE PROCEDURE drogueria_schema.sp_annulment_transaction(
	IN in_transaction_reference character varying,
	OUT out_message character varying,
	OUT out_status boolean,
	OUT out_transaction_id bigint)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
	v_transaction_id BIGINT;
	v_type_trx VARCHAR;

	-- Declaramos 'rec' como un record para poder almacenar los resultados del SELECT
	rec RECORD;

	v_p_quantity INT;
	v_return_quantity_in_product INT;

BEGIN
	-- Obtener el ID de la transacción según la referencia proporcionada
	SELECT id, type_trx INTO v_transaction_id, v_type_trx
	FROM drogueria_schema.transactions
	WHERE reference = in_transaction_reference;

	IF v_transaction_id IS NOT NULL
	THEN
		IF v_type_trx != 'ANNULMENT'
		THEN
			-- Iterar sobre todos los productos asociados a la transacción
			FOR rec IN
				SELECT product_id, quantity
				FROM drogueria_schema.transaction_item
				WHERE transaction_id = v_transaction_id
			LOOP
				-- Obtener la cantidad actual de stock del producto
				SELECT stock INTO v_p_quantity
				FROM drogueria_schema.products
				WHERE id = rec.product_id;

				-- Calcular la nueva cantidad de stock
				v_return_quantity_in_product := v_p_quantity + rec.quantity;

				-- Actualizar el stock del producto
				UPDATE drogueria_schema.products
				SET stock = v_return_quantity_in_product
				WHERE id = rec.product_id;
			END LOOP;

			-- Actualizar la transacción a 'ANNULMENT'
			UPDATE drogueria_schema.transactions
			SET date_update_trx = TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'), type_trx = 'ANNULMENT'
			WHERE id = v_transaction_id;

			out_message := 'Transaction successfully canceled';
			out_status := TRUE;
			out_transaction_id := v_transaction_id;
		ELSE
			-- Si la transacción no existe
			out_message := 'The transaction has already been canceled';
			out_status := FALSE;
			out_transaction_id := v_transaction_id;
		END IF;
	ELSE
		-- Si la transacción no existe
		out_message := 'The transaction does not exist';
		out_status := FALSE;
		out_transaction_id := NULL;
	END IF;
END;
$BODY$;
