insert into vote (id, vote_state, user_id, quote_id)
values (7, 'UPVOTE', 6, 1),
       (8, 'UPVOTE', 6, 2),
       (9, 'UPVOTE', 6, 3),
       (10, 'DOWNVOTE', 6, 4);

update quote set vote_counter = 1 where id in (1, 2, 3);
update quote set vote_counter = -1 where id = 4;

alter sequence hibernate_sequence restart with 11;