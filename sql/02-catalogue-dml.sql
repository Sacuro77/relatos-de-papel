from pathlib import Path
from datetime import date, datetime

books = [
("El principito","Antoine de Saint-Exupéry","1943-04-06","Fábula","9780156012195",5.0,True,25,12.99,"Obra literaria clásica utilizada como libro de prueba para el catálogo.","https://example.com/el-principito.jpg"),
("Cien años de soledad","Gabriel García Márquez","1967-05-30","Novela","9780307474728",4.9,True,18,18.50,"Novela representativa del realismo mágico latinoamericano.","https://example.com/cien-anos.jpg"),
("Don Quijote de la Mancha","Miguel de Cervantes","1605-01-16","Clásico","9788491050291",4.8,True,15,22.00,"Obra fundamental de la literatura española.","https://example.com/quijote.jpg"),
("La ciudad y los perros","Mario Vargas Llosa","1963-10-10","Novela","9788466333579",4.5,True,12,17.75,"Novela sobre disciplina, violencia y juventud.","https://example.com/ciudad-perros.jpg"),
("Rayuela","Julio Cortázar","1963-06-28","Novela","9788437604572",4.6,True,10,19.90,"Novela experimental de la literatura hispanoamericana.","https://example.com/rayuela.jpg"),
("Pedro Páramo","Juan Rulfo","1955-03-19","Novela","9788437604183",4.7,True,14,14.80,"Obra breve y profunda de la narrativa mexicana.","https://example.com/pedro-paramo.jpg"),
("La casa de los espíritus","Isabel Allende","1982-01-01","Novela","9780553383805",4.5,True,16,16.40,"Saga familiar con elementos históricos y mágicos.","https://example.com/casa-espiritus.jpg"),
("Ficciones","Jorge Luis Borges","1944-01-01","Cuento","9780802130303",4.8,True,9,13.60,"Colección de relatos filosóficos y fantásticos.","https://example.com/ficciones.jpg"),
("El Aleph","Jorge Luis Borges","1949-01-01","Cuento","9788420633121",4.7,True,11,13.20,"Relatos sobre infinito, memoria y símbolos.","https://example.com/aleph.jpg"),
("Crónica de una muerte anunciada","Gabriel García Márquez","1981-01-01","Novela","9781400034956",4.4,True,22,11.99,"Novela corta sobre destino y responsabilidad colectiva.","https://example.com/cronica.jpg"),
("1984","George Orwell","1949-06-08","Distopía","9780451524935",4.8,True,30,15.50,"Distopía política sobre vigilancia y control social.","https://example.com/1984.jpg"),
("Rebelión en la granja","George Orwell","1945-08-17","Sátira","9780451526342",4.6,True,28,10.99,"Fábula política sobre poder y corrupción.","https://example.com/rebelion-granja.jpg"),
("Un mundo feliz","Aldous Huxley","1932-01-01","Distopía","9780060850524",4.5,True,17,14.50,"Distopía sobre tecnología, placer y control.","https://example.com/mundo-feliz.jpg"),
("Fahrenheit 451","Ray Bradbury","1953-10-19","Distopía","9781451673319",4.6,True,19,13.99,"Novela sobre censura, libros y pensamiento crítico.","https://example.com/fahrenheit.jpg"),
("Matar a un ruiseñor","Harper Lee","1960-07-11","Novela","9780061120084",4.8,True,13,15.25,"Novela sobre justicia, infancia y prejuicio racial.","https://example.com/ruisenor.jpg"),
("Orgullo y prejuicio","Jane Austen","1813-01-28","Romance","9780141439518",4.7,True,20,12.70,"Clásico sobre sociedad, amor y malentendidos.","https://example.com/orgullo.jpg"),
("Sentido y sensibilidad","Jane Austen","1811-10-30","Romance","9780141439662",4.4,True,12,12.30,"Novela sobre familia, emociones y normas sociales.","https://example.com/sensibilidad.jpg"),
("Jane Eyre","Charlotte Brontë","1847-10-16","Novela","9780141441146",4.6,True,14,13.40,"Historia de independencia, amor y dignidad.","https://example.com/jane-eyre.jpg"),
("Cumbres borrascosas","Emily Brontë","1847-12-01","Novela","9780141439556",4.4,True,10,13.10,"Novela intensa sobre pasión y conflicto familiar.","https://example.com/cumbres.jpg"),
("Drácula","Bram Stoker","1897-05-26","Terror","9780141439846",4.5,True,18,14.20,"Clásico gótico sobre el mito del vampiro.","https://example.com/dracula.jpg"),
("Frankenstein","Mary Shelley","1818-01-01","Terror","9780486282114",4.6,True,21,11.50,"Novela sobre ciencia, creación y responsabilidad.","https://example.com/frankenstein.jpg"),
("La metamorfosis","Franz Kafka","1915-01-01","Novela corta","9780553213690",4.5,True,24,9.99,"Relato sobre alienación y transformación.","https://example.com/metamorfosis.jpg"),
("El proceso","Franz Kafka","1925-01-01","Novela","9780805209990",4.3,True,8,13.80,"Novela sobre burocracia, culpa y absurdo.","https://example.com/proceso.jpg"),
("Los miserables","Victor Hugo","1862-01-01","Clásico","9780451419439",4.8,True,9,24.90,"Gran novela social sobre justicia y redención.","https://example.com/miserables.jpg"),
("Nuestra Señora de París","Victor Hugo","1831-01-14","Clásico","9780140443530",4.4,True,7,18.50,"Novela histórica ambientada en París medieval.","https://example.com/notre-dame.jpg"),
("El conde de Montecristo","Alexandre Dumas","1844-08-28","Aventura","9780140449266",4.9,True,12,23.40,"Aventura sobre traición, paciencia y venganza.","https://example.com/montecristo.jpg"),
("Los tres mosqueteros","Alexandre Dumas","1844-03-01","Aventura","9780141442341",4.6,True,13,16.75,"Aventura de honor, amistad y política.","https://example.com/mosqueteros.jpg"),
("La isla del tesoro","Robert Louis Stevenson","1883-11-14","Aventura","9780141321004",4.5,True,20,10.80,"Clásico juvenil de piratas y aventuras.","https://example.com/isla-tesoro.jpg"),
("Viaje al centro de la Tierra","Julio Verne","1864-11-25","Aventura","9780486440880",4.4,True,16,11.90,"Aventura científica subterránea.","https://example.com/viaje-centro.jpg"),
("Veinte mil leguas de viaje submarino","Julio Verne","1870-06-20","Aventura","9780451531698",4.6,True,18,12.60,"Aventura submarina con el capitán Nemo.","https://example.com/veinte-mil.jpg"),
("La vuelta al mundo en 80 días","Julio Verne","1873-01-30","Aventura","9780140449068",4.5,True,15,11.70,"Viaje global lleno de desafíos.","https://example.com/vuelta-mundo.jpg"),
("Moby Dick","Herman Melville","1851-10-18","Aventura","9781503280786",4.2,True,9,15.99,"Novela marítima sobre obsesión y destino.","https://example.com/moby-dick.jpg"),
("El viejo y el mar","Ernest Hemingway","1952-09-01","Novela corta","9780684801223",4.4,True,22,10.20,"Relato sobre perseverancia y dignidad.","https://example.com/viejo-mar.jpg"),
("Adiós a las armas","Ernest Hemingway","1929-09-27","Novela","9780684801469",4.2,True,10,14.30,"Historia de amor en tiempos de guerra.","https://example.com/adios-armas.jpg"),
("El gran Gatsby","F. Scott Fitzgerald","1925-04-10","Novela","9780743273565",4.4,True,20,13.99,"Retrato del sueño americano y sus contradicciones.","https://example.com/gatsby.jpg"),
("Las uvas de la ira","John Steinbeck","1939-04-14","Novela","9780143039433",4.5,True,8,17.50,"Novela social sobre migración y pobreza.","https://example.com/uvas-ira.jpg"),
("De ratones y hombres","John Steinbeck","1937-01-01","Novela corta","9780140177398",4.4,True,18,10.99,"Historia de amistad y fragilidad humana.","https://example.com/ratones-hombres.jpg"),
("El guardián entre el centeno","J. D. Salinger","1951-07-16","Novela","9780316769488",4.1,True,14,12.99,"Novela sobre adolescencia y desencanto.","https://example.com/guardian.jpg"),
("El señor de los anillos I","J. R. R. Tolkien","1954-07-29","Fantasía","9780547928210",4.9,True,25,18.99,"Inicio de una épica fantástica en la Tierra Media.","https://example.com/lotr1.jpg"),
("El señor de los anillos II","J. R. R. Tolkien","1954-11-11","Fantasía","9780547928203",4.9,True,24,18.99,"Continuación de la misión del anillo.","https://example.com/lotr2.jpg"),
("El señor de los anillos III","J. R. R. Tolkien","1955-10-20","Fantasía","9780547928197",4.9,True,23,18.99,"Cierre de la épica de la Tierra Media.","https://example.com/lotr3.jpg"),
("El Hobbit","J. R. R. Tolkien","1937-09-21","Fantasía","9780547928227",4.8,True,26,15.99,"Aventura de Bilbo Bolsón hacia la Montaña Solitaria.","https://example.com/hobbit.jpg"),
("Harry Potter y la piedra filosofal","J. K. Rowling","1997-06-26","Fantasía","9788478884452",4.8,True,30,14.99,"Inicio de la saga de magia y amistad.","https://example.com/hp1.jpg"),
("Harry Potter y la cámara secreta","J. K. Rowling","1998-07-02","Fantasía","9788478884957",4.7,True,28,14.99,"Segundo año de Harry en Hogwarts.","https://example.com/hp2.jpg"),
("Harry Potter y el prisionero de Azkaban","J. K. Rowling","1999-07-08","Fantasía","9788478885190",4.8,True,27,15.99,"Tercer año con secretos del pasado.","https://example.com/hp3.jpg"),
("Harry Potter y el cáliz de fuego","J. K. Rowling","2000-07-08","Fantasía","9788478886456",4.8,True,26,16.99,"Torneo mágico y regreso de amenazas mayores.","https://example.com/hp4.jpg"),
("Juego de tronos","George R. R. Martin","1996-08-06","Fantasía","9780553573404",4.7,True,17,19.99,"Intriga política y fantasía épica.","https://example.com/juego-tronos.jpg"),
("Choque de reyes","George R. R. Martin","1998-11-16","Fantasía","9780553579901",4.6,True,15,19.99,"Continuación de las guerras por el trono.","https://example.com/choque-reyes.jpg"),
("Dune","Frank Herbert","1965-08-01","Ciencia ficción","9780441172719",4.8,True,21,17.99,"Ciencia ficción política y ecológica en Arrakis.","https://example.com/dune.jpg"),
("Fundación","Isaac Asimov","1951-06-01","Ciencia ficción","9780553293357",4.7,True,19,13.99,"Saga sobre civilización, ciencia y futuro.","https://example.com/fundacion.jpg"),
("Yo, robot","Isaac Asimov","1950-12-02","Ciencia ficción","9780553382563",4.5,True,22,12.99,"Relatos sobre robots y leyes de la robótica.","https://example.com/yo-robot.jpg"),
("Solaris","Stanisław Lem","1961-01-01","Ciencia ficción","9780156027601",4.4,True,11,14.60,"Exploración filosófica de contacto alienígena.","https://example.com/solaris.jpg"),
("Neuromante","William Gibson","1984-07-01","Ciencia ficción","9780441569595",4.3,True,12,14.99,"Obra fundacional del cyberpunk.","https://example.com/neuromante.jpg"),
("La mano izquierda de la oscuridad","Ursula K. Le Guin","1969-03-01","Ciencia ficción","9780441478125",4.6,True,10,15.20,"Exploración social y política en otro mundo.","https://example.com/mano-izquierda.jpg"),
("Los desposeídos","Ursula K. Le Guin","1974-05-01","Ciencia ficción","9780061054884",4.5,True,9,15.30,"Novela sobre utopía, anarquía y ciencia.","https://example.com/desposeidos.jpg"),
("La historia interminable","Michael Ende","1979-09-01","Fantasía","9780140386332",4.7,True,18,14.99,"Aventura fantástica sobre imaginación y lectura.","https://example.com/historia-interminable.jpg"),
("Momo","Michael Ende","1973-01-01","Fantasía","9780140317534",4.6,True,16,13.50,"Fábula sobre tiempo, amistad y humanidad.","https://example.com/momo.jpg"),
("Alicia en el país de las maravillas","Lewis Carroll","1865-11-26","Fantasía","9781503222687",4.5,True,20,9.99,"Clásico fantástico de lógica y absurdo.","https://example.com/alicia.jpg"),
("El mago de Oz","L. Frank Baum","1900-05-17","Fantasía","9780486291169",4.4,True,18,10.50,"Viaje fantástico hacia la Ciudad Esmeralda.","https://example.com/oz.jpg"),
("Las aventuras de Tom Sawyer","Mark Twain","1876-01-01","Aventura","9780486400776",4.3,True,17,9.80,"Aventuras juveniles en el río Mississippi.","https://example.com/tom-sawyer.jpg"),
("Huckleberry Finn","Mark Twain","1884-12-10","Aventura","9780486280615",4.4,True,12,10.20,"Novela de viaje, libertad y crítica social.","https://example.com/huck-finn.jpg"),
("La llamada de la selva","Jack London","1903-07-01","Aventura","9780486264721",4.3,True,15,8.99,"Aventura de supervivencia en la naturaleza.","https://example.com/llamada-selva.jpg"),
("Colmillo blanco","Jack London","1906-05-01","Aventura","9780486269689",4.4,True,16,9.25,"Historia de un lobo-perro y su adaptación.","https://example.com/colmillo.jpg"),
("Sherlock Holmes: Estudio en escarlata","Arthur Conan Doyle","1887-11-01","Misterio","9780140439083",4.5,True,21,10.99,"Primera aparición de Sherlock Holmes.","https://example.com/estudio-escarlata.jpg"),
("El sabueso de los Baskerville","Arthur Conan Doyle","1902-04-01","Misterio","9780140437867",4.6,True,19,11.40,"Misterio clásico de Sherlock Holmes.","https://example.com/baskerville.jpg"),
("Asesinato en el Orient Express","Agatha Christie","1934-01-01","Misterio","9780062693662",4.7,True,25,12.99,"Caso emblemático de Hercule Poirot.","https://example.com/orient-express.jpg"),
("Diez negritos","Agatha Christie","1939-11-06","Misterio","9780062073488",4.7,True,20,12.99,"Novela de misterio en una isla aislada.","https://example.com/diez-negritos.jpg"),
("El nombre de la rosa","Umberto Eco","1980-01-01","Misterio","9780156001311",4.6,True,14,16.99,"Misterio medieval, filosofía y literatura.","https://example.com/nombre-rosa.jpg"),
("El perfume","Patrick Süskind","1985-01-01","Novela","9780375725845",4.5,True,13,14.80,"Novela sobre obsesión, olor e identidad.","https://example.com/perfume.jpg"),
("Ensayo sobre la ceguera","José Saramago","1995-01-01","Novela","9780156007757",4.6,True,15,15.50,"Alegoría social sobre fragilidad humana.","https://example.com/ceguera.jpg"),
("El evangelio según Jesucristo","José Saramago","1991-01-01","Novela","9780156001410",4.3,True,8,15.00,"Relectura literaria de tradición religiosa.","https://example.com/evangelio.jpg"),
("La tregua","Mario Benedetti","1960-01-01","Novela","9788432210877",4.5,True,14,11.99,"Diario íntimo sobre amor y rutina.","https://example.com/tregua.jpg"),
("Gracias por el fuego","Mario Benedetti","1965-01-01","Novela","9788432210815",4.2,True,9,12.50,"Novela sobre conflicto familiar y social.","https://example.com/gracias-fuego.jpg"),
("El túnel","Ernesto Sabato","1948-01-01","Novela","9780140189926",4.4,True,15,11.90,"Novela psicológica sobre obsesión y soledad.","https://example.com/tunel.jpg"),
("Sobre héroes y tumbas","Ernesto Sabato","1961-01-01","Novela","9788437604961",4.3,True,8,17.80,"Novela compleja sobre historia y oscuridad interior.","https://example.com/heroes-tumbas.jpg"),
("Los detectives salvajes","Roberto Bolaño","1998-01-01","Novela","9780307266866",4.5,True,10,18.90,"Novela coral sobre poesía y búsqueda.","https://example.com/detectives.jpg"),
("2666","Roberto Bolaño","2004-01-01","Novela","9780312429218",4.6,True,7,24.50,"Novela extensa sobre violencia, literatura y misterio.","https://example.com/2666.jpg"),
("La sombra del viento","Carlos Ruiz Zafón","2001-01-01","Novela","9780143034902",4.7,True,23,16.99,"Misterio literario en la Barcelona de posguerra.","https://example.com/sombra-viento.jpg"),
("El juego del ángel","Carlos Ruiz Zafón","2008-04-17","Novela","9780307472595",4.4,True,12,16.50,"Intriga literaria y secretos editoriales.","https://example.com/juego-angel.jpg"),
("Patria","Fernando Aramburu","2016-09-06","Novela","9788420426563",4.6,True,18,18.00,"Novela sobre memoria, violencia y convivencia.","https://example.com/patria.jpg"),
("Nada","Carmen Laforet","1945-01-01","Novela","9788423328413",4.3,True,11,12.90,"Retrato de posguerra y juventud en Barcelona.","https://example.com/nada.jpg"),
("La familia de Pascual Duarte","Camilo José Cela","1942-01-01","Novela","9788423343973",4.2,True,10,12.40,"Novela intensa del tremendismo español.","https://example.com/pascual-duarte.jpg"),
("La colmena","Camilo José Cela","1951-01-01","Novela","9788423343805",4.1,True,9,12.70,"Retrato coral del Madrid de posguerra.","https://example.com/colmena.jpg"),
("Platero y yo","Juan Ramón Jiménez","1914-01-01","Poesía narrativa","9788437604183",4.4,True,13,10.50,"Obra lírica sobre ternura y naturaleza.","https://example.com/platero.jpg"),
("Campos de Castilla","Antonio Machado","1912-01-01","Poesía","9788467033270",4.5,True,12,10.90,"Poemario sobre paisaje, memoria y España.","https://example.com/campos.jpg"),
("Veinte poemas de amor y una canción desesperada","Pablo Neruda","1924-01-01","Poesía","9788437603674",4.5,True,20,9.99,"Poemario amoroso de gran difusión.","https://example.com/neruda20.jpg"),
("Residencia en la tierra","Pablo Neruda","1933-01-01","Poesía","9788437604244",4.3,True,8,11.80,"Poemario de imágenes densas y existenciales.","https://example.com/residencia.jpg"),
("Poeta en Nueva York","Federico García Lorca","1940-01-01","Poesía","9788437600826",4.6,True,9,12.00,"Poemario sobre modernidad, angustia y ciudad.","https://example.com/poeta-ny.jpg"),
("Bodas de sangre","Federico García Lorca","1933-03-08","Teatro","9788437600543",4.5,True,15,8.99,"Tragedia teatral sobre destino y pasión.","https://example.com/bodas.jpg"),
("La casa de Bernarda Alba","Federico García Lorca","1945-01-01","Teatro","9788437602240",4.7,True,17,8.99,"Drama sobre represión, autoridad y deseo.","https://example.com/bernarda.jpg"),
("Hamlet","William Shakespeare","1603-01-01","Teatro","9780743477123",4.8,True,20,9.99,"Tragedia sobre duda, poder y venganza.","https://example.com/hamlet.jpg"),
("Romeo y Julieta","William Shakespeare","1597-01-01","Teatro","9780743477116",4.5,True,22,9.99,"Tragedia clásica de amor y conflicto familiar.","https://example.com/romeo.jpg"),
("Macbeth","William Shakespeare","1606-01-01","Teatro","9780743477109",4.6,True,18,9.99,"Tragedia sobre ambición y culpa.","https://example.com/macbeth.jpg"),
("Otelo","William Shakespeare","1604-01-01","Teatro","9780743477550",4.4,True,13,9.99,"Tragedia sobre celos y manipulación.","https://example.com/otelo.jpg"),
("La Odisea","Homero","-0700-01-01","Épica","9780140268867",4.7,True,11,14.99,"Poema épico sobre regreso, astucia y aventura.","https://example.com/odisea.jpg"),
("La Ilíada","Homero","-0750-01-01","Épica","9780140275360",4.6,True,10,14.99,"Poema épico sobre guerra, honor y destino.","https://example.com/iliada.jpg"),
("La Eneida","Virgilio","-0019-01-01","Épica","9780140449327",4.4,True,8,13.99,"Poema épico fundacional de Roma.","https://example.com/eneida.jpg"),
("Divina comedia","Dante Alighieri","1320-01-01","Poesía épica","9780140448955",4.8,True,12,17.99,"Viaje alegórico por Infierno, Purgatorio y Paraíso.","https://example.com/divina-comedia.jpg"),
("El arte de la guerra","Sun Tzu","-0500-01-01","Ensayo","9781590302255",4.4,True,30,8.99,"Tratado clásico de estrategia.","https://example.com/arte-guerra.jpg"),
("Meditaciones","Marco Aurelio","0180-01-01","Filosofía","9780486298238",4.7,True,24,9.99,"Reflexiones estoicas sobre vida y virtud.","https://example.com/meditaciones.jpg"),
("Ética a Nicómaco","Aristóteles","-0340-01-01","Filosofía","9780140449495",4.4,True,10,12.99,"Tratado sobre virtud, felicidad y razón práctica.","https://example.com/etica.jpg"),
("La República","Platón","-0380-01-01","Filosofía","9780140455113",4.5,True,11,13.50,"Diálogo sobre justicia, política y conocimiento.","https://example.com/republica.jpg"),
("Así habló Zaratustra","Friedrich Nietzsche","1883-01-01","Filosofía","9780140441185",4.3,True,9,12.90,"Obra filosófica y poética sobre transformación.","https://example.com/zaratustra.jpg"),
("Más allá del bien y del mal","Friedrich Nietzsche","1886-01-01","Filosofía","9780140449235",4.4,True,10,12.50,"Crítica a la moral tradicional.","https://example.com/bien-mal.jpg"),
("El extranjero","Albert Camus","1942-01-01","Novela filosófica","9780679720201",4.5,True,17,11.99,"Novela sobre absurdo, indiferencia y juicio social.","https://example.com/extranjero.jpg"),
("La peste","Albert Camus","1947-06-10","Novela filosófica","9780679720218",4.6,True,14,13.99,"Alegoría sobre enfermedad, solidaridad y condición humana.","https://example.com/peste.jpg"),
("Ser y tiempo","Martin Heidegger","1927-01-01","Filosofía","9780061575594",4.1,True,5,22.99,"Obra filosófica sobre existencia y temporalidad.","https://example.com/ser-tiempo.jpg"),
("El ser y la nada","Jean-Paul Sartre","1943-01-01","Filosofía","9780671867805",4.0,True,6,21.99,"Tratado existencialista sobre libertad y conciencia.","https://example.com/ser-nada.jpg"),
("El segundo sexo","Simone de Beauvoir","1949-01-01","Ensayo","9780307277787",4.6,True,10,18.99,"Ensayo fundamental sobre género y sociedad.","https://example.com/segundo-sexo.jpg"),
("Una habitación propia","Virginia Woolf","1929-10-24","Ensayo","9780156787338",4.5,True,16,10.99,"Ensayo sobre escritura, independencia y mujeres.","https://example.com/habitacion.jpg"),
("Al faro","Virginia Woolf","1927-05-05","Novela","9780156907392",4.2,True,9,12.99,"Novela modernista sobre memoria y percepción.","https://example.com/al-faro.jpg"),
("Mrs Dalloway","Virginia Woolf","1925-05-14","Novela","9780156628709",4.3,True,11,12.99,"Novela modernista sobre un día y una conciencia.","https://example.com/dalloway.jpg"),
("Ulises","James Joyce","1922-02-02","Novela","9780199535675",4.1,True,7,19.99,"Novela modernista de gran complejidad formal.","https://example.com/ulises.jpg"),
("Retrato del artista adolescente","James Joyce","1916-12-29","Novela","9780142437346",4.2,True,8,12.99,"Formación artística e identidad personal.","https://example.com/retrato-artista.jpg"),
("En busca del tiempo perdido","Marcel Proust","1913-11-14","Novela","9780375751547",4.4,True,5,29.99,"Exploración monumental de memoria y tiempo.","https://example.com/proust.jpg"),
("Madame Bovary","Gustave Flaubert","1856-12-15","Novela","9780140449129",4.4,True,13,12.99,"Novela sobre deseo, frustración y sociedad.","https://example.com/bovary.jpg"),
("Rojo y negro","Stendhal","1830-01-01","Novela","9780140447644",4.3,True,9,13.99,"Novela sobre ambición social y pasión.","https://example.com/rojo-negro.jpg"),
("Guerra y paz","León Tolstói","1869-01-01","Novela","9781400079988",4.8,True,6,24.99,"Novela monumental sobre historia, familia y guerra.","https://example.com/guerra-paz.jpg"),
("Anna Karénina","León Tolstói","1877-01-01","Novela","9780143035008",4.7,True,8,19.99,"Novela sobre amor, sociedad y tragedia.","https://example.com/anna.jpg"),
("Crimen y castigo","Fiódor Dostoievski","1866-01-01","Novela","9780140449136",4.8,True,14,16.99,"Novela psicológica sobre culpa y redención.","https://example.com/crimen.jpg"),
("Los hermanos Karamázov","Fiódor Dostoievski","1880-01-01","Novela","9780374528379",4.8,True,8,19.99,"Novela filosófica sobre fe, familia y moral.","https://example.com/karamazov.jpg"),
("El idiota","Fiódor Dostoievski","1869-01-01","Novela","9780375702242",4.4,True,7,17.99,"Novela sobre bondad, sociedad y conflicto interior.","https://example.com/idiota.jpg"),
("Almas muertas","Nikolái Gógol","1842-01-01","Novela","9780140448078",4.2,True,6,14.99,"Sátira social de la Rusia imperial.","https://example.com/almas.jpg"),
("La madre","Máximo Gorki","1906-01-01","Novela","9781410106710",4.1,True,8,13.40,"Novela social sobre conciencia política.","https://example.com/madre.jpg"),
("El maestro y Margarita","Mijaíl Bulgákov","1967-01-01","Novela","9780141180144",4.7,True,10,15.99,"Sátira fantástica y filosófica.","https://example.com/maestro-margarita.jpg"),
("Doctor Zhivago","Borís Pasternak","1957-01-01","Novela","9780679774389",4.3,True,7,16.99,"Novela sobre revolución, amor e historia.","https://example.com/zhivago.jpg"),
("El tambor de hojalata","Günter Grass","1959-01-01","Novela","9780156029042",4.2,True,6,17.50,"Novela alegórica de la historia alemana.","https://example.com/tambor.jpg"),
("La montaña mágica","Thomas Mann","1924-01-01","Novela","9780679772873",4.3,True,5,19.90,"Novela sobre tiempo, enfermedad y pensamiento europeo.","https://example.com/montana.jpg"),
("Muerte en Venecia","Thomas Mann","1912-01-01","Novela corta","9780486434124",4.1,True,9,9.99,"Relato sobre belleza, obsesión y decadencia.","https://example.com/venecia.jpg"),
("Siddhartha","Hermann Hesse","1922-01-01","Novela filosófica","9780553208849",4.5,True,21,10.99,"Búsqueda espiritual y autoconocimiento.","https://example.com/siddhartha.jpg"),
("El lobo estepario","Hermann Hesse","1927-01-01","Novela filosófica","9780312278670",4.3,True,12,12.99,"Novela sobre identidad y crisis interior.","https://example.com/lobo.jpg"),
("Demian","Hermann Hesse","1919-01-01","Novela filosófica","9780060931919",4.4,True,14,11.99,"Historia de formación espiritual y simbólica.","https://example.com/demian.jpg"),
("El principito oculto","Autor de prueba","2020-01-01","Prueba", "9780000000001",3.5,False,5,7.99,"Libro oculto para probar visibilidad falsa.","https://example.com/oculto.jpg"),
("Libro sin stock","Autor de prueba","2021-01-01","Prueba","9780000000002",3.0,True,0,6.99,"Libro visible sin stock para probar validaciones.","https://example.com/sin-stock.jpg")
]

def sql_escape(s: str) -> str:
    return s.replace("'", "''")

lines = []
lines.append("-- Datos iniciales del catálogo de libros")
lines.append("-- Archivo generado para la Actividad 2 - Relatos de Papel")
lines.append("-- Contiene más de 100 libros para poblar catalogue_db")
lines.append("")
lines.append("INSERT INTO books (title, author, publication_date, category, isbn, rating, visible, stock, price, description, image_url, created_at, updated_at)")
lines.append("VALUES")
value_lines = []
for b in books:
    title, author, pub, cat, isbn, rating, visible, stock, price, desc, img = b
    visible_sql = "TRUE" if visible else "FALSE"
    # handle BCE invalid dates by converting to positive placeholder? PostgreSQL accepts BC with date? easier use 0001? But Java LocalDate min? DML SQL may accept BC? better adjust invalid negative dates to 0001-01-01 and descriptions still okay.
    if pub.startswith("-"):
        pub_sql = "0001-01-01"
    else:
        pub_sql = pub
    value_lines.append(
        f"('{sql_escape(title)}', '{sql_escape(author)}', '{pub_sql}', '{sql_escape(cat)}', '{isbn}', {rating:.1f}, {visible_sql}, {stock}, {price:.2f}, '{sql_escape(desc)}', '{img}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)"
    )
lines.append(",\n".join(value_lines) + ";")
lines.append("")
lines.append("SELECT setval('books_id_seq', (SELECT MAX(id) FROM books));")
content = "\n".join(lines)

path = Path("/mnt/data/02-catalogue-dml.sql")
path.write_text(content, encoding="utf-8")
len(books), path.stat().st_size
