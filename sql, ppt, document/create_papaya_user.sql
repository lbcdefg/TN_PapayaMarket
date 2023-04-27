alter session set "_oracle_script"=true;
create user tn_papaya identified by java1234;
grant connect, resource, unlimited tablespace to tn_papaya;

--유저 tn_papaya 생성
