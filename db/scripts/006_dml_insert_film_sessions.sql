insert into film_sessions(id, film_id, halls_id, start_time, end_time, price)
values  (1, 3, 2, timestamp '2024-02-16 12:00', timestamp '2024-02-16 14:00', 400),
        (2, 6, 3, timestamp '2024-02-16 12:00', timestamp '2024-02-16 14:00', 300),

        (3, 2, 1, timestamp '2024-02-16 14:30', timestamp '2024-02-16 17:00', 400),
        (4, 4, 2, timestamp '2024-02-16 14:30', timestamp '2024-02-16 17:00', 400),
        (5, 1, 3, timestamp '2024-02-16 14:30', timestamp '2024-02-16 17:00', 300),

        (6, 1, 1, timestamp '2024-02-16 17:30', timestamp '2024-02-16 19:00', 700),
        (7, 3, 2, timestamp '2024-02-16 17:30', timestamp '2024-02-16 19:00', 400),
        (8, 4, 3, timestamp '2024-02-16 17:30', timestamp '2024-02-16 19:00', 500),

        (9, 2, 1, timestamp '2024-02-16 19:30', timestamp '2024-02-16 21:30', 700),
        (10, 5, 2, timestamp '2024-02-16 19:30', timestamp '2024-02-16 22:00', 400),
        (11, 1, 3, timestamp '2024-02-16 22:00', timestamp '2024-02-16 23:30', 600);