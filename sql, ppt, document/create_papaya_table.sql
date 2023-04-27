drop table CHATCONTENT;
drop table CHAT;
drop table DEAL;
drop table FAVORITE;
drop table POST;
drop table CATEGORYPM;
drop table USERINFO;
drop sequence POST_SEQ;
drop sequence FAVORITE_SEQ;
drop sequence DEAL_SEQ;
drop sequence CHAT_SEQ;

create table USERINFO(
  USERID varchar2(30) constraint USERINFO_PK primary key, 
  USERNICK varchar2(20) constraint USERINFO_USERNICK_NN not null,
  constraint USERINFO_USERNICK_UQ unique(USERNICK),
  USERPW varchar2(20) constraint USERINFO_USERPW_NN not null,
  USERADDR varchar2(200) constraint USERINFO_USERADDR_NN not null,
  USERTEL varchar2(13) constraint USERINFO_USERTEL_NN not null,
  constraint USERINFO_USERTEL_UQ unique(USERTEL),
  USERP number(2,1)
);

create table CATEGORYPM(
  CATEGORYPMNO number(2) constraint CATEGORYPMNO primary key,
  CATEGORYPMTYPE varchar2(30)
);

create table POST(
  POSTNO number constraint POST_PK primary key,
  USERID varchar2(30) constraint POST_USERID_NN not null,
  POSTTITLE varchar2(50) constraint POST_POSTTITLE_NN  not null,
  CATEGORYPMNO number(2) constraint POST_CATEGORYPMNO_NN not null,
  POSTCOST number constraint POST_POSTCOST_NN not null,
  POSTCONTENT varchar2(900) constraint POST_POSTCONTENT_NN not null,
  POSTIMAGE varchar2(900),
  POSTST varchar2(15) constraint POST_POSTST_NN not null,
  POSTREDATE date,
  POSTCDATE date,
  POSTDELETE varchar2(1) constraint POST_POSTDELETE_NN not null,
  constraint POST_USERID_FK  foreign key(USERID) references  USERINFO(USERID) on delete cascade,
  constraint POST_CATEGORYPMNO_FK foreign key(CATEGORYPMNO) references  CATEGORYPM(CATEGORYPMNO) on delete cascade
);

create sequence Post_SEQ increment by 1 start with 1 nocache;

create table FAVORITE(
  FAVORNO number constraint FAVORITE_PK primary key,
  POSTNO number constraint FAVORITE_POSTNO_NN not null,
  USERID varchar2(30) constraint FAVORITE_USERID_NN not null,
  constraint FAVORITE_POSTNO_FK  foreign key(POSTNO) references POST(POSTNO) on delete cascade,
  constraint FAVORITE_USERID_FK foreign key(USERID) references USERINFO(USERID) on delete cascade
);
create sequence Favorite_SEQ increment by 1 start with 1 nocache;

create table DEAL(
  DEALNO number constraint DEAL_PK primary key,
  DEALSELLERID varchar2(30) constraint DEAL_DEALSELLERID_NN not null,
  DEALBUYERID varchar2(30) constraint DEAL_DEALBUYERID_NN not null,
  POSTNO number constraint DEAL_POSTNO_NN not null,
  DEALDATE date,
  SELLERP number(2,1),
  BUYERP number(2,1),
  DEALDELETE varchar2(1) constraint DEAL_DEALDELETE_NN not null,
  constraint DEAL_DEALSELLERID_FK foreign key(DEALSELLERID) references USERINFO(USERID) on delete cascade,
  constraint DEAL_DEALBUYERID_FK foreign key(DEALBUYERID) references USERINFO(USERID) on delete cascade,
  constraint DEAL_POSTNO_FK foreign key(POSTNO) references POST(POSTNO) on delete cascade
);
create sequence DEAL_SEQ increment by 1 start with 1 nocache;

create table CHAT(
  CHATNO number constraint CHAT_PK primary key,
  CHATSELLERID varchar2(30) constraint CHAT_CHATSELLERID_NN not null,
  CHATBUYERID varchar2(30) constraint CHAT_CHATBUYERID_NN not null,
  CHATSELLEROUT varchar(1),
  CHATBUYEROUT varchar(1),
  POSTNO number constraint CHAT_POSTNO_NN not null,
  constraint CHAT_CHATSELLERID_FK foreign key(CHATSELLERID) references USERINFO(USERID) on delete cascade,
  constraint CHAT_CHATBUYERID_FK foreign key(CHATBUYERID) references USERINFO(USERID) on delete cascade,
  constraint CHAT_POSTNO_FK foreign key(POSTNO) references POST(POSTNO) on delete cascade
);
create sequence CHAT_SEQ increment by 1 start with 1 nocache;

create table CHATCONTENT(
  CHATNO number constraint CHATCONTENT_CHATNO_NN not null,
  CHATCON varchar2(1000),
  CHATUDATE date,
  CHATSPEAKER varchar2(30),
  constraint CHATCONTENT_CHATNO_FK foreign key(CHATNO) references Chat(CHATNO) on delete cascade,
  constraint CHATCONTENT_CHATSPEAKER_FK foreign key(CHATSPEAKER) references USERINFO(USERID) on delete cascade
);

insert into CATEGORYPM values(10,'전자');
insert into CATEGORYPM values(20,'의류');
insert into CATEGORYPM values(30,'도서');
insert into CATEGORYPM values(40,'가구');
insert into CATEGORYPM values(50,'기타');