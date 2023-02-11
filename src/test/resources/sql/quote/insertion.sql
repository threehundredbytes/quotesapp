insert into quote (id, text, vote_count, posted_by_id, created_at, modified_at)
values (2, 'The greatest glory in living lies not in never falling, but in rising every time we fall.', 0, 1, '2023-02-10', '2023-02-10'),
       (3, 'The way to get started is to quit talking and begin doing.', 0, 1, '2023-02-10', '2023-02-10'),
       (4, 'Your time is limited, so don''t waste it living someone else''s life. Don''t be trapped by dogma – which is living with the results of other people''s thinking.', 0, 1, '2023-02-10', '2023-02-10'),
       (5, 'If life were predictable it would cease to be life, and be without flavor.', 0, 1, '2023-02-10', '2023-02-10'),
       (6, 'If you look at what you have in life, you''ll always have more. If you look at what you don''t have in life, you''ll never have enough.', 0, 1, '2023-02-10', '2023-02-10');

alter sequence hibernate_sequence restart with 7;