insert into quote (id, text, vote_counter)
values (1, 'The greatest glory in living lies not in never falling, but in rising every time we fall.', 0),
       (2, 'The way to get started is to quit talking and begin doing.', 0),
       (3, 'Your time is limited, so don''t waste it living someone else''s life. Don''t be trapped by dogma – which is living with the results of other people''s thinking.', 0),
       (4, 'If life were predictable it would cease to be life, and be without flavor.', 0),
       (5, 'If you look at what you have in life, you''ll always have more. If you look at what you don''t have in life, you''ll never have enough.', 0);

alter sequence hibernate_sequence restart with 6;