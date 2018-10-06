create user tineuser
alter user tineuser with encrypted password 'password';
ALTER DATABASE tinecommerce OWNER TO tineuser;