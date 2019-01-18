INSERT INTO BUNDLE VALUES(1, '2019-01-01 12:00:00', '2019-01-01 12:00:00', 1, 'description', '', 'My Bundle', 10);
INSERT INTO BUNDLE VALUES(11, '2019-01-10 23:23:21.262', '2019-01-10 23:23:21.262',	0, '',	'http://i.imgur.com/aitnt7i.png	Popular Multiplayers',	25.00);
INSERT INTO BUNDLE VALUES(18,	'2019-01-10 23:23:21.275',	'2019-01-10 23:23:21.275',	0,	'',	'http://www.wallpapersxl.com/get/2532890',	'Role Playing Games',	75.00);

INSERT INTO BUNDLE_PRODUCT VALUES(11, 12);
INSERT INTO BUNDLE_PRODUCT VALUES(11, 14);
INSERT INTO BUNDLE_PRODUCT VALUES(11, 16);
INSERT INTO BUNDLE_PRODUCT VALUES(18, 16);
INSERT INTO BUNDLE_PRODUCT VALUES(18, 19);
INSERT INTO BUNDLE_PRODUCT VALUES(18, 21);
INSERT INTO BUNDLE_PRODUCT VALUES(18, 23);

INSERT INTO CART VALUES(33,	'2019-01-11 11:17:32.385',	'2019-01-11 11:17:32.385',	0,	30);

INSERT INTO CART_DETAIL VALUES(35,	'2019-01-11 11:17:32.403', '2019-01-11 11:17:32.403',	0,	1,	3);

INSERT INTO DEVELOPER VALUES(1,	'2019-01-10 23:23:21.19',	'2019-01-10 23:23:21.19',	0,	'',	'',	'Default Developer');
INSERT INTO DEVELOPER VALUES(4,	'2019-01-10 23:23:21.243',	'2019-01-10 23:23:21.243',	0,	'',	'',	'Code Force');
INSERT INTO DEVELOPER VALUES(8,	'2019-01-10 23:23:21.251',	'2019-01-10 23:23:21.251',	0,	'',	'',	'Paradox Development Studio');
INSERT INTO DEVELOPER VALUES(13,	'2019-01-10 23:23:21.262',	'2019-01-10 23:23:21.262',	0,	'',	'',	'Valve Corporation');
INSERT INTO DEVELOPER VALUES(17,	'2019-01-10 23:23:21.263',	'2019-01-10 23:23:21.263',	0,	'',	'',	'TaleWorlds Entertainment');
INSERT INTO DEVELOPER VALUES(20,	'2019-01-10 23:23:21.275',	'2019-01-10 23:23:21.275',	0,	'',	'',	'Obsidian Entertainment');
INSERT INTO DEVELOPER VALUES(24,	'2019-01-10 23:23:21.276',	'2019-01-10 23:23:21.276',	0,	'',	'',	'Bethesda Game Studios');

INSERT INTO ORDER_HISTORY VALUES(34,	'2019-01-11 11:17:32.401',	'2019-01-11 11:17:32.401',	0,	'Store Cart Purchase',	10.00,	30);

INSERT INTO ORDER_HISTORY_CART_DETAILS VALUES(34, 35);

INSERT INTO PRODUCT VALUES(3,	'2019-01-10 23:23:21.242',	'2019-01-10 23:23:21.242',	0,	'Space 4X',	'http://www.codeforce.co.nz/favicon.ico	Distant Worlds',	10.00,	'https://www.youtube.com/embed/eUtjeVL58yY',	4,	5);
INSERT INTO PRODUCT VALUES(6,	'2019-01-10 23:23:21.249',	'2019-01-10 23:23:21.249',	0,	'Space 4X',	'http://www.etcwiki.org/images/thumb/d/da/Distant_worlds_universe_large_box.jpg/300px-Distant_worlds_universe_large_box.jpg	Distant Worlds Universe',	50.00,	'https://www.youtube.com/embed/eUtjeVL58yY',	4,	5);
INSERT INTO PRODUCT VALUES(7,	'2019-01-10 23:23:21.251',	'2019-01-10 23:23:21.251',	0,	'strategy',	'http://orig02.deviantart.net/0725/f/2012/232/1/1/europa_universalis_4_by_nerces-d5bt8m1.png',	'Europa Universalis IV',	25.00,	'https://www.youtube.com/embed/ONWTGEZKzn8',	8,	9);
INSERT INTO PRODUCT VALUES(10,	'2019-01-10 23:23:21.255',	'2019-01-10 23:23:21.255',	0,	'Space 4X',	'http://orig11.deviantart.net/abe4/f/2016/133/3/f/icon_stellaris_by_hazzbrogaming-da2b15o.png',	'Stellaris',	40.00,	'https://www.youtube.com/embed/KanCiSGxSKM'	8,	9);
INSERT INTO PRODUCT VALUES(12,	'2019-01-10 23:23:21.262',	'2019-01-10 23:23:21.262',	0,	'RTS',	'http://orig06.deviantart.net/f3d1/f/2012/192/0/9/dota_2_icon_by_snaapsnaap-d56t8wg.png',	'Dota 2',	0.00,	'https://www.youtube.com/embed/-cSFPIwMEq4',	13,	15);
INSERT INTO PRODUCT VALUES(14,	'2019-01-10 23:23:21.262',	'2019-01-10 23:23:21.262',	0,	'FPS',	'http://orig04.deviantart.net/b9d0/f/2015/179/e/2/counter_strike_global_offensive_icon_by_ru_devlin-d8z2vpe.png	Counter-Strike',	10.00,	'https://www.youtube.com/embed/edYCtaNueQY',	13,	15);
INSERT INTO PRODUCT VALUES(16,	'2019-01-10 23:23:21.263',	'2019-01-10 23:23:21.263',	0,	'ARPG',	'http://www.iconarchive.com/download/i539/3xhumed/mega-games-pack-25/Mount-Blade-1.ico',	'Mount & Blade',	10.00,	'https://www.youtube.com/embed/sX37QA9o-Co',	17,	9);
INSERT INTO PRODUCT VALUES(19,	'2019-01-10 23:23:21.275',   '2019-01-10 23:23:21.275',	0,	'ARPG',	'http://orig05.deviantart.net/39e2/f/2011/045/7/3/fallout_new_vegas_by_madrapper-d39imeq.png',	'Fallout: New Vegas',	20.00,	'https://www.youtube.com/embed/cIzOttk6Dv4',	20,	22);
INSERT INTO PRODUCT VALUES(21,	'2019-01-10 23:23:21.275',	'2019-01-10 23:23:21.275',	0,	'RPG',	'http://orig12.deviantart.net/f509/f/2015/082/2/6/pillars_of_eternity___icon_by_blagoicons-d8mswa6.png',	'Pillars of Eternity',	30.00,	'https://www.youtube.com/embed/uclz-RhyMpo',	20,	9);
INSERT INTO PRODUCT VALUES(23,	'2019-01-10 23:23:21.275',	'2019-01-10 23:23:21.275',	0,	'ARPG',	'http://img06.deviantart.net/5f8f/i/2011/046/4/3/elder_scrolls_v___skyrim_icon_by_bonscha-d39n68b.png',	'The Elder Scrolls V: Skyrim',	30.00,	'https://www.youtube.com/embed/PjqsYzBrP-M',	24,	22);

INSERT INTO PUBLISHER VALUES(2,	'2019-01-10 23:23:21.22',	'2019-01-10 23:23:21.22',	0,	'',	'',	'Default Publisher');
INSERT INTO PUBLISHER VALUES(5,	'2019-01-10 23:23:21.243',	'2019-01-10 23:23:21.243',	0,	'',	'',	'Matrix Games');
INSERT INTO PUBLISHER VALUES(9,	'2019-01-10 23:23:21.252',	'2019-01-10 23:23:21.252',	0,	'',	'',	'Paradox Interactive');
INSERT INTO PUBLISHER VALUES(15,	'2019-01-10 23:23:21.262',	'2019-01-10 23:23:21.262',	0,	'',	'',	'Valve Corporation');
INSERT INTO PUBLISHER VALUES(22,	'2019-01-10 23:23:21.275',	'2019-01-10 23:23:21.275',	0,	'',	'',	'Bethesda Softworks');

INSERT INTO ROLE VALUES(25,	'2019-01-10 23:23:21.284',	'2019-01-10 23:23:21.622',	3,	'CUSTOMER');
INSERT INTO ROLE VALUES(26,	'2019-01-10 23:23:21.287',	'2019-01-10 23:23:21.626',	1,	'ADMIN');
INSERT INTO ROLE VALUES(27,	'2019-01-10 23:23:21.29',	'2019-01-10 23:23:21.29',	0,	'TEMPORARY');

INSERT INTO ROLE_USER VALUES(25, 28);
INSERT INTO ROLE_USER VALUES(25, 29);
INSERT INTO ROLE_USER VALUES(25, 30);
INSERT INTO ROLE_USER VALUES(26, 30);

INSERT INTO USER VALUES(28,	'2019-01-10 23:23:21.398',	'2019-01-10 23:23:21.398',	0,	null,	null,	null,	null,	null,	'customer1@gmail.com',	'$2a$10$MmXa2P8BtNpIbfilzjgRy.ifLr6RVp2XxwA9OmZAnxJ3c1QB.WxAG',	'customer1',	null);
INSERT INTO USER VALUES(29,	'2019-01-10 23:23:21.501',	'2019-01-10 23:23:21.501',	0,	null,	null,	null,	null,	null,	'customer2@gmail.com',	'$2a$10$PPIkOFRzhNAflqJZ7PK/m.dyHsasL0RLnWz3XB6X8QiHYk7KjXAjq',	'customer2',	null);
INSERT INTO USER VALUES(30,	'2019-01-10 23:23:21.601',	'2019-01-11 11:17:32.414',	1,	null,	null,	null,	null,	null,	'admin1@gmail.com',	'$2a$10$aiG91I2epzsgfnw2qCwj.uU.GNnABlDy8AsX/rXqYqtd8aHuLrN6W',	'admin',	33);

INSERT INTO USER_ORDER_HISTORIES VALUES(30, 34);