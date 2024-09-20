-- PROCEDURE: drogueria_schema.sp_annulment_transaction(character varying)

-- DROP PROCEDURE IF EXISTS drogueria_schema.sp_annulment_transaction(character varying);

CREATE OR REPLACE PROCEDURE drogueria_schema.sp_annulment_transaction(
	IN in_transaction_reference character varying,
	OUT out_message character varying,
	OUT out_status boolean,
	OUT out_transaction_id bigint)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
	
	v_transaction_id BIGINT;
	
	v_ti_product_id BIGINT;
	v_ti_quantity BIGINT;
	
	v_p_quantity INT;
	
	v_return_quantity_in_product INT;

BEGIN
	SELECT id INTO v_transaction_id
	FROM drogueria_schema.transactions
	WHERE reference = in_transaction_reference;
	
	IF v_transaction_id IS NOT NULL
	THEN
		UPDATE drogueria_schema.transactions
		SET date_update_trx = TO_CHAR(NOW(), 'DD-MM-YYYY HH24:MI:SS'), type_trx = 'ANNULMENT'
		WHERE id = v_transaction_id;
		
		SELECT id, quantity INTO v_ti_product_id, v_ti_quantity
		FROM drogueria_schema.transaction_item
		WHERE transaction_id = v_transaction_id;
		
		SELECT stock INTO v_p_quantity
		FROM drogueria_schema.products
		WHERE id = v_ti_product_id;
		
		v_return_quantity_in_product := v_p_quantity + v_ti_quantity;
		
		UPDATE drogueria_schema.products
		SET stock = v_return_quantity_in_product
		WHERE id = v_ti_product_id;
		
		out_message := 'Transaction successfully canceled';
		out_status := TRUE;
		out_transaction_id := v_transaction_id;
	ELSE
		out_message := 'The transaction does not exist';
		out_status := FALSE;
		out_transaction_id := NULL;
	END IF;
END;
$BODY$;
ALTER PROCEDURE drogueria_schema.sp_annulment_transaction(character varying)
    OWNER TO postgres;
