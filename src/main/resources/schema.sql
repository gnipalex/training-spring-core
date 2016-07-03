
drop table tickets;
drop table orderEntries;
drop table orders;
drop table users;
drop table eventAirDates;
drop table eventAirDatesAuditoriums;
drop table events;


CREATE TABLE users (
    id bigint primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    birthday timestamp,
    firstName varchar(100),
    lastName varchar(100),
    email varchar(50) not null unique
);

CREATE TABLE orders (
    id bigint primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    dateTime timestamp not null,
    userId bigint constraint order_user_fk 
        references users(id) on delete set null on update restrict,
    description varchar(100)
);

CREATE TABLE orderEntries (
    id bigint primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    basePrice double not null,
    discount double not null,
    orderId bigint constraint orderEntry_order_fk
        references orders(id) on delete cascade on update restrict
);

CREATE TABLE events (
    id bigint primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    name varchar(50) not null unique,
    basePrice double not null,
    rating varchar(10) check (rating in ('LOW', 'MID', 'HIGH'))
);

CREATE TABLE eventAirDates (
    eventId bigint not null constraint event_airdates_fk 
        references events(id) on delete cascade on update restrict,
    airDate timestamp not null
);

ALTER TABLE eventAirDates ADD CONSTRAINT eventId_airDate_unique 
    unique (eventId, airDate);

CREATE TABLE eventAirDatesAuditoriums (
    eventId bigint not null constraint event_airdates_auditorium_fk 
        references events(id) on delete cascade on update restrict,
    airDate timestamp not null,
    auditorium varchar(20) not null
);

ALTER TABLE eventAirDatesAuditoriums ADD CONSTRAINT eventId_airDate_auditorium_unique 
    unique (eventId, airDate, auditorium);
    
CREATE TABLE tickets (
    id bigint primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
    dateTime timestamp not null,
    seat bigint not null,
    orderEntryId bigint not null constraint ticket_orderEntry_fk 
        references orderEntries(id) on delete cascade on update restrict,
    eventId bigint not null constraint ticket_event_fk
        references events(id) on delete cascade on update restrict
);

ALTER TABLE tickets ADD CONSTRAINT datetime_seat_eventId_unique
    unique (dateTime, seat, eventId);
    
