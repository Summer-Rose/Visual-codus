--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: courses; Type: TABLE; Schema: public; Owner: mozawa; Tablespace:
--

CREATE TABLE courses (
    course_id integer NOT NULL,
    course_name character varying
);


ALTER TABLE courses OWNER TO mozawa;

--
-- Name: courses_course_id_seq; Type: SEQUENCE; Schema: public; Owner: mozawa
--

CREATE SEQUENCE courses_course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE courses_course_id_seq OWNER TO mozawa;

--
-- Name: courses_course_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mozawa
--

ALTER SEQUENCE courses_course_id_seq OWNED BY courses.course_id;


--
-- Name: students; Type: TABLE; Schema: public; Owner: mozawa; Tablespace:
--

CREATE TABLE students (
    student_id integer NOT NULL,
    course_id integer,
    age integer,
    gender character varying,
    origin character varying,
    distance_traveled character varying,
    salary_before integer
);


ALTER TABLE students OWNER TO mozawa;

--
-- Name: students_student_id_seq; Type: SEQUENCE; Schema: public; Owner: mozawa
--

CREATE SEQUENCE students_student_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE students_student_id_seq OWNER TO mozawa;

--
-- Name: students_student_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mozawa
--

ALTER SEQUENCE students_student_id_seq OWNED BY students.student_id;


--
-- Name: course_id; Type: DEFAULT; Schema: public; Owner: mozawa
--

ALTER TABLE ONLY courses ALTER COLUMN course_id SET DEFAULT nextval('courses_course_id_seq'::regclass);


--
-- Name: student_id; Type: DEFAULT; Schema: public; Owner: mozawa
--

ALTER TABLE ONLY students ALTER COLUMN student_id SET DEFAULT nextval('students_student_id_seq'::regclass);


--
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: mozawa
--

COPY courses (course_id, course_name) FROM stdin;
\.


--
-- Name: courses_course_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mozawa
--

SELECT pg_catalog.setval('courses_course_id_seq', 1, false);


--
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: mozawa
--

COPY students (student_id, course_id, age, gender, origin, distance_traveled, salary_before) FROM stdin;
\.


--
-- Name: students_student_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mozawa
--

SELECT pg_catalog.setval('students_student_id_seq', 1, false);


--
-- Name: courses_pkey; Type: CONSTRAINT; Schema: public; Owner: mozawa; Tablespace:
--

ALTER TABLE ONLY courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (course_id);


--
-- Name: students_pkey; Type: CONSTRAINT; Schema: public; Owner: mozawa; Tablespace:
--

ALTER TABLE ONLY students
    ADD CONSTRAINT students_pkey PRIMARY KEY (student_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: mozawa
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM mozawa;
GRANT ALL ON SCHEMA public TO mozawa;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--
