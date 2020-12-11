CREATE TABLE "proposals" (
  "id" SERIAL PRIMARY KEY,
  "community" int,
  "title" text,
  "description" text,
  "date" timestamp,
  "author" text
);

CREATE TABLE "communities" (
  "id" SERIAL PRIMARY KEY,
  "name" text,
  "description" text
);

CREATE TABLE "community2users" (
  "user" text,
  "community" int
);

CREATE TABLE "comments" (
  "id" SERIAL PRIMARY KEY,
  "text" text,
  "author" text,
  "proposal" int
);

CREATE TABLE "users" (
  "id" text PRIMARY KEY,
  "username" text,
  "password" text
);

CREATE TABLE "liked2users" (
  "user" text,
  "proposal" int
);

CREATE TABLE "disliked2users" (
  "user" text,
  "proposal" int
);

ALTER TABLE "proposals" ADD FOREIGN KEY ("community") REFERENCES "communities" ("id");

ALTER TABLE "proposals" ADD FOREIGN KEY ("author") REFERENCES "users" ("id");

ALTER TABLE "community2users" ADD FOREIGN KEY ("user") REFERENCES "users" ("id");

ALTER TABLE "community2users" ADD FOREIGN KEY ("community") REFERENCES "communities" ("id");

ALTER TABLE "comments" ADD FOREIGN KEY ("author") REFERENCES "users" ("id");

ALTER TABLE "comments" ADD FOREIGN KEY ("proposal") REFERENCES "proposals" ("id");

ALTER TABLE "liked2users" ADD FOREIGN KEY ("user") REFERENCES "users" ("id");

ALTER TABLE "liked2users" ADD FOREIGN KEY ("proposal") REFERENCES "proposals" ("id");

ALTER TABLE "disliked2users" ADD FOREIGN KEY ("user") REFERENCES "users" ("id");

ALTER TABLE "disliked2users" ADD FOREIGN KEY ("proposal") REFERENCES "proposals" ("id");
