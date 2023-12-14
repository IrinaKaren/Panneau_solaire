database panneau_solaire;

------------------------------ TABLE -------------------------------------------
CREATE TABLE source_solaire (
    id serial primary key,
    nom varchar(50),
    puissance decimal
); 

CREATE TABLE luminosite(
    id serial primary key,
    date_luminosite timestamp,
    capacite decimal
);

CREATE TABLE batterie(
    id serial primary key,
    idSS int references source_solaire(id),
    capacite decimal
);

CREATE TABLE classe(
    id serial primary key,
    nom varchar(50),
    nbr_personne int
);

CREATE TABLE presence(
    id serial primary key,
    idClasse int references classe(id),
    date date,
    demi_journee varchar(50),
    nbr int
);

CREATE TABLE classe_ps(
    idSS int references source_solaire(id),
    idClasse int references classe(id)
);

CREATE TABLE coupure(
    id serial primary key,
    idSS int references source_solaire(id), 
    date_coupure timestamp,    
    jour varchar(30)
);

------------------------------ DATA --------------------------------------------
INSERT INTO source_solaire (nom,puissance)
VALUES
('SS1',15000),
('SS2',10000);

INSERT INTO luminosite (date_luminosite, capacite)
VALUES
    ('2023-12-01 08:00:00', 3),
    ('2023-12-01 09:00:00', 4),
    ('2023-12-01 10:00:00', 4),
    ('2023-12-01 11:00:00', 4.5),
    ('2023-12-01 12:00:00', 5),
    ('2023-12-01 13:00:00', 5.8),
    ('2023-12-01 14:00:00', 6),
    ('2023-12-01 15:00:00', 7.2),
    ('2023-12-01 16:00:00', 7),
    ('2023-12-01 17:00:00', 6.3);

INSERT INTO batterie (idSS, capacite)
VALUES
    (1,2700),    
    (2,3500);

INSERT INTO classe (nom,nbr_personne)
VALUES
    ('S1 Info',120),
    ('S1 Design',110),
    ('S3 Design',56);

INSERT INTO presence (idClasse,date,demi_journee,nbr)
VALUES
    (1,'2023-12-01','matin',120),
    (1,'2023-12-01','apres_midi',70),
    (2,'2023-12-01','matin',110),
    (2,'2023-12-01','apres_midi',42),
    (3,'2023-12-01','matin',55),
    (3,'2023-12-01','apres_midi',23);

INSERT INTO classe_ps (idSS,idClasse)
VALUES
    (1,1),
    (1,2),
    (2,3);

INSERT INTO coupure (idSS, date_coupure, jour)
VALUES
    (1, '2023-12-01 16:30:00', lower(to_char('2023-12-01 16:30:00'::timestamp, 'Day'))),
    (2, '2023-12-01 16:45:00', lower(to_char('2023-12-01 16:45:00'::timestamp, 'Day')));

SELECT to_char('2023-11-29 08:00:00'::timestamp, 'Day');

SELECT EXTRACT(HOUR FROM '2023-12-01 16:30:00'::timestamp) || ':' || EXTRACT(MINUTE FROM '2023-12-01 16:30:00'::timestamp) || ':' || EXTRACT(SECOND FROM '2023-12-01 16:30:00'::timestamp);

------------------------------ VIEW --------------------------------------------
-- coupure mi relier an'zareo rehetra
CREATE VIEW v_coupure AS
SELECT 
    coupure.id as idcoupure,
    coupure.jour as jour,
    coupure.date_coupure as date_coupure,
    source_solaire.id as idss,
    source_solaire.nom as source_solaire,
    source_solaire.puissance as ss_puissance,
    luminosite.id as idlum, 
    luminosite.date_luminosite as date_luminosite,
    luminosite.capacite as lm_capacite,
    batterie.id as idbat,
    batterie.capacite as bat_capacite,
    classe.id as idclasse,  
    classe.nom as classe,
    classe.nbr_personne as nbr_personne,
    presence.date as date_presence, 
    presence.demi_journee as dj_presence,
    presence.nbr as nbr_presence
FROM 
    coupure
JOIN 
    source_solaire on source_solaire.id = coupure.idss 
JOIN
    luminosite on DATE_TRUNC('day', luminosite.date_luminosite) = DATE_TRUNC('day', coupure.date_coupure)
JOIN 
    batterie on  batterie.idss = source_solaire.id
JOIN
    classe_ps on classe_ps.idss = source_solaire.id
JOIN 
    classe on  classe.id = classe_ps.idClasse 
JOIN 
    presence on (presence.idclasse = classe.id and DATE_TRUNC('day', presence.date) = DATE_TRUNC('day',coupure.date_coupure));

--- izay mitovy ny source solaire dia atambatra 1 ny nbr presents

-- tsy par classe fa par source_solaire
CREATE VIEW v_presence AS
SELECT 
    coupure.jour as jour,
    coupure.date_coupure as date_coupure,
    source_solaire.nom as source_solaire,
    sum(presence.nbr) as nbr_presence,
    presence.demi_journee as dj_presence,
FROM
    coupure
JOIN 
    source_solaire on source_solaire.id = coupure.idss 
JOIN 
    presence on DATE_TRUNC('day', presence.date) = DATE_TRUNC('day',coupure.date_coupure)
GROUP BY coupure.jour, coupure.date_coupure, source_solaire.nom, presence.demi_journee;

SELECT DISTINCT (source_solaire,classe,nbr_personne,date_presence,dj_presence,nbr_presence)
	source_solaire,
	classe,
	nbr_personne,
	date_presence,
	dj_presence,
	nbr_presence
FROM v_coupure
------------- DONNEE DE TEST ---------------------------------------------------
INSERT INTO luminosite (date_luminosite, capacite)
VALUES
    ('2023-11-08 08:00:00', 3),
    ('2023-11-08 09:00:00', 4),
    ('2023-11-08 10:00:00', 4),
    ('2023-11-08 11:00:00', 4.5),
    ('2023-11-08 12:00:00', 5),
    ('2023-11-08 13:00:00', 5.8),
    ('2023-11-08 14:00:00', 6),
    ('2023-11-08 15:00:00', 7.2),
    ('2023-11-08 16:00:00', 7),
    ('2023-11-08 17:00:00', 6.3);

INSERT INTO presence (idClasse,date,demi_journee,nbr)
VALUES
    (1,'2023-11-24','matin',120),
    (1,'2023-11-24','apres_midi',70),
    (2,'2023-11-24','matin',110),
    (2,'2023-11-24','apres_midi',42),
    (3,'2023-11-24','matin',55),
    (3,'2023-11-24','apres_midi',23);

INSERT INTO coupure (idSS, date_coupure, jour)
VALUES
    (1, '2023-11-24 16:30:00', lower(to_char('2023-11-24 16:30:00'::timestamp, 'Day'))),
    (2, '2023-11-24 16:45:00', lower(to_char('2023-11-24 16:45:00'::timestamp, 'Day')));

SELECT *
FROM v_presence
WHERE lower(trim(jour)) = lower('friday');

WHERE lower(jour) = lower(to_char(date_coupure, 'Day')) and source_solaire ='SS1' and dj_presence='matin'


--------------------- TEST DONNEE ROTSY ----------------------------------------
INSERT INTO source_solaire (nom,puissance)
VALUES
('TestDonnee',40000);

INSERT INTO batterie (idSS, capacite)
VALUES
    (1,30000);

INSERT INTO luminosite (date_luminosite, capacite)
VALUES
    ('2023-12-14 08:00:00', 4.8),
    ('2023-12-14 09:00:00', 3.8),
    ('2023-12-14 10:00:00', 7.9),
    ('2023-12-14 11:00:00', 8),
    ('2023-12-14 12:00:00', 9),
    ('2023-12-14 13:00:00', 7),
    ('2023-12-14 14:00:00', 2.9),
    ('2023-12-14 15:00:00', 7.1),
    ('2023-12-14 16:00:00', 5.6),
    ('2023-12-14 17:00:00', 5);

INSERT INTO classe (nom,nbr_personne)
VALUES
    ('Salle test',500);

INSERT INTO classe_ps (idSS,idClasse)
VALUES
    (1,1);

INSERT INTO presence (idClasse,date,demi_journee,nbr)
VALUES
    (1,'2023-12-14','matin',450),
    (1,'2023-12-14','apres_midi',430);

INSERT INTO coupure (idSS, date_coupure, jour)
VALUES
    (1, '2023-12-14 14:36:00', lower(to_char('2023-12-14 14:36:00'::timestamp, 'Day')));







