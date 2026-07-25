update player_season set pitcher = false where pitcher is null;
alter table player_season alter column pitcher set not null;
alter table player_season drop constraint uq_player_season;
alter table player_season add constraint uq_player_season unique (mlb_player_id, season, pitcher);