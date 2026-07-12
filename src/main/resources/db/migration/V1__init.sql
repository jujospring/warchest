create table player_season (
	id		bigserial	primary key,
	mlb_player_id 	integer 	not null,
	season 		integer		not null,
	full_name	text		not null,
	team_abbrev	text,
	position	text,
	war		numeric(4, 1),
	salary		bigint,
	updated_at	timestamptz	not null default now(),
	constraint uq_player_season unique (mlb_player_id, season)
);

create index idx_player_season_season on player_season (season);

