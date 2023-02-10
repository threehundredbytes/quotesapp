delete from vote;

update quote set vote_counter = 0;

alter sequence hibernate_sequence restart with 7;