delete from vote;

update quote set vote_count = 0;

alter sequence hibernate_sequence restart with 7;