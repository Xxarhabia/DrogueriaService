CREATE TABLE drogueria_schema.sp_error_logs(
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	procedure_name VARCHAR(255),
	message_error VARCHAR(255),
	error_date VARCHAR(80)
);