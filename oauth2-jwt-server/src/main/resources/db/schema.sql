-- DROP SCHEMA if exists authorization_server_schema CASCADE;

CREATE SCHEMA if not exists authorization_server_schema AUTHORIZATION postgres;

COMMENT ON SCHEMA authorization_server_schema IS 'authorization server database (registered users schema) ';

-- Drop table

-- DROP TABLE authorization_server_schema.external_ids;

create table authorization_server_schema.external_ids (
	uuid uuid not null,
	CONSTRAINT external_ids_pk PRIMARY KEY (uuid)
);

-- DROP SEQUENCE authorization_server_schema.users_id_seq;

create sequence authorization_server_schema.users_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 3
	CACHE 1
	NO CYCLE;

-- Drop table

-- DROP TABLE authorization_server_schema.users;

create table authorization_server_schema.users (
	id bigint not null default nextval('authorization_server_schema.users_id_seq'),
	uuid uuid not null,
	"type" varchar(12) not null,

	"role" varchar(16) not null DEFAULT 'USER'::character varying,
	"name" varchar(60) null,
	given_name varchar(150) null,
	family_name varchar(150) null,

	enabled bool not null,
	account_non_locked bool not null,
	account_non_expired bool not null,
	credentials_non_expired bool not null,
	account_confirmed bool not null,
	email varchar(160) not null,
	last_active bigint not null,
	password_hash varchar(160) null,

	CONSTRAINT users_pk PRIMARY KEY (id)
);

create unique index users_email_idx ON authorization_server_schema.users USING btree (email);
alter table authorization_server_schema.users ADD CONSTRAINT users_fk FOREIGN KEY (uuid) references authorization_server_schema.external_ids(uuid) ON UPDATE cascade ON DELETE cascade;


