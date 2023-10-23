INSERT INTO blogs.users (id, username, status)
VALUES (nextval('blogs.users_seq'), 'test-user', 'ACTIVE');

INSERT INTO blogs.posts (id, title, description, slug, topic, author_id, likes)
VALUES (nextval('blogs.posts_seq'), 'Test title', 'Test description', 'test', 'test', 1, 0);