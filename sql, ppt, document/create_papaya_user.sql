alter session set "_oracle_script"=true;
create user tn_papaya identified by java1234;
grant connect, resource, unlimited tablespace to tn_papaya;

--���� tn_papaya ����
