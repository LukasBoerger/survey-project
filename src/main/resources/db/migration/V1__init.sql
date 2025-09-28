create table if not exists users (
                                     id uuid primary key,
                                     email varchar(255) unique not null,
    display_name varchar(255),
    password_hash varchar(255) not null,
    email_verified boolean default false not null,
    created_at timestamptz default now() not null
    );
create unique index if not exists uk_users_email on users(lower(email));
create table if not exists surveys (
                                       id uuid primary key,
                                       owner_id uuid not null references users(id),
    title varchar(255) not null,
    public_id varchar(20) unique not null,
    created_at timestamptz default now(),
    updated_at timestamptz default now()
    );

create table if not exists questions (
                                         id uuid primary key,
                                         survey_id uuid not null references surveys(id) on delete cascade,
    type varchar(30) not null,   -- STAR|EMOJI|TEXT
    prompt text not null,
    position int not null
    );
create index if not exists idx_questions_survey on questions(survey_id);
