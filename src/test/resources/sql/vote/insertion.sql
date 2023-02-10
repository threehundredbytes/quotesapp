insert into vote (id, vote_state, user_id, quote_id)
values (7, 'UPVOTE', 1, 2),
       (8, 'UPVOTE', 1, 3),
       (9, 'UPVOTE', 1, 4),
       (10, 'DOWNVOTE', 1, 5);

update quote set vote_counter = 1 where id in (2, 3, 4);
update quote set vote_counter = -1 where id = 5;

alter sequence hibernate_sequence restart with 11;