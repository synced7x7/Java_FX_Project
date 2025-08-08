DROP TABLE IF EXISTS movie_cast;
DROP TABLE IF EXISTS watchlist;
DROP TABLE IF EXISTS movies;
CREATE DATABASE IF NOT EXISTS moviedb;
USE moviedb;

CREATE TABLE movies
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    title        VARCHAR(255),
    genre        VARCHAR(100),
    cast         TEXT,
    duration     VARCHAR(20),
    imdb_rating DOUBLE,
    summary      TEXT,
    coverImage   VARCHAR(255),
    detailImage1 VARCHAR(255),
    detailImage2 VARCHAR(255)
);

INSERT INTO movies (title, genre, cast, duration, imdb_rating, summary, coverImage, detailImage1, detailImage2)
VALUES ('A Silent Voice', 'Drama', 'Miyu Irino, Saori Hayami, Aoi Yuki', '2 hrs 10 min', 8.8,
        'A poignant tale about a boy who bullied a deaf girl in elementary school and seeks forgiveness years later. The film explores themes of bullying, disability, and redemption with emotional depth and powerful character development.',
        'covers/a_silent_voice.jpg', 'details/a_silent_voice_1.png', 'details/a_silent_voice_2.png'),

       ('Demon Slayer: Mugen Train', 'Action, Fantasy', 'Natsuki Hanae, Akari Kito, Hiro Shimono', '1 hr 57 min', 8.4,
        'The Demon Slayer Corps boards the Mugen Train to investigate disappearances. Tanjiro and his friends face powerful demons while protecting innocent passengers in an intense and emotional journey of sacrifice and courage.',
        'covers/mugen_train.jpg', 'details/mugen_train_1.png', 'details/mugen_train_2.jpeg'),

       ('Demon Slayer: Infinity Castle', 'Action, Fantasy', 'Natsuki Hanae, Akari Kito, Hiro Shimono', '2 hrs 10 min', 9.2,
        'The continuation of Demon Slayer’s story featuring Tanjiro and his friends infiltrating the Infinity Castle to defeat Muzan Kibutsuji and his Twelve Kizuki in an epic battle for survival.',
        'covers/demon_slayer_infinity_castle.jpg', 'details/infinity_castle_1.png', 'details/infinity_castle_2.jpg'),

       ('Fate/stay night: Heavens Feel III', 'Action, Fantasy, Drama', 'Noriaki Sugiyama, Kana Ueda, Mai Kadowaki',
        '2 hrs', 8.1,
        'The darkest route of the Fate series follows Shirou Emiya’s battle against sinister forces while protecting Sakura Matou, unraveling tragic secrets that question love, sacrifice, and heroism.',
        'covers/fate_hf3.jpg', 'details/fate_hf3_1.png', 'details/fate_hf3_2.jpg'),

       ('I Want to Eat Your Pancreas', 'Drama, Romance', 'Mahiro Takasugi, Lynn, Yukiyo Fujii', '1 hr 45 min', 8.0,
        'A touching story about a high school boy who discovers the secret diary of a girl suffering from a terminal illness. Their friendship blossoms into a bittersweet relationship, exploring themes of life, death, and connection.',
        'covers/eat_your_pancreas.jpg', 'details/eat_your_pancreas_1.png', 'details/eat_your_pancreas_2.jpg'),

       ('The Last: Naruto the Movie', 'Action, Adventure, Romance', 'Junko Takeuchi, Chie Nakamura, Noriaki Sugiyama',
        '1 hr 56 min', 7.7,
        'Set after the Fourth Great Ninja War, Naruto and his friends face a new threat from the Moon’s descent. The film explores Naruto’s evolving feelings for Hinata and the fate of the ninja world.',
        'covers/last_naruto.jpg', 'details/last_naruto_1.png', 'details/last_naruto_2.jpg'),

       ('Violet Evergarden: The Movie', 'Drama, Fantasy', 'Yui Ishikawa, Minako Kotobuki', '1 hr 30 min', 8.2,
        'Violet, a former soldier adjusting to civilian life, embarks on a journey of emotional healing as she reconnects with her past and those she cares about, learning the true meaning of love and words.',
        'covers/violet_evergarden.jpg', 'details/violet_evergarden_1.png', 'details/violet_evergarden_2.jpg'),

       ('Your Name', 'Romance, Fantasy', 'Ryunosuke Kamiki, Mone Kamishiraishi', '1 hr 20 min', 8.9,
        'A boy in Tokyo and a girl in the countryside mysteriously begin swapping bodies. As they search for each other, they uncover a deeper connection transcending time and space, blending stunning visuals with heartfelt emotion.',
        'covers/your_name.jpg', 'details/your_name_1.png', 'details/your_name_2.jpg'),

       ('Weathering With You', 'Romance, Fantasy', 'Kotaro Daigo, Nana Mori', '1 hr 52 min', 7.9,
        'A runaway boy in Tokyo meets a girl who can manipulate the weather. Together, they confront supernatural forces and tough choices in a story about love, climate, and hope.',
        'covers/weathering_with_you.jpg', 'details/weathering_with_you_1.png', 'details/weathering_with_you_2.jpg'),

       ('Suzume', 'Adventure, Fantasy', 'Nanoka Hara, Hokuto Matsumura', '2 hrs 2 min', 7.5,
        'A young girl Suzume embarks on a journey to close mysterious doors causing disasters throughout Japan, blending fantasy with coming-of-age themes and emotional growth.',
        'covers/suzume.jpg', 'details/suzume_1.png', 'details/suzume_2.jpg'),

       ('A Whisker Away', 'Fantasy, Romance', 'Mirai Shida, Kento Yamazaki', '1 hr 44 min', 6.5,
        'Miyo transforms into a cat to get closer to her crush. As realities blur, she must confront her feelings and the danger of losing herself in this magical romance.',
        'covers/whisker_away.jpg', 'details/whisker_away_1.png', 'details/whisker_away_2.jpg'),

       ('Spirited Away', 'Fantasy, Adventure', 'Rumi Hiiragi, Miyu Irino', '2 hrs 5 min', 8.9,
        'A young girl Chihiro enters a mysterious spirit world to save her parents and find her way home. The film is a masterful blend of fantasy, folklore, and coming-of-age storytelling.',
        'covers/spirited_away.jpg', 'details/spirited_away_1.png', 'details/spirited_away_2.jpg'),

       ('Haikyu! The Dumpster Battle', 'Sports, Drama', 'Kensho Ono, Kaito Ishikawa', '1 hr 55 min', 7.8,
        'The Karasuno volleyball team faces a critical match against rival Nekoma, testing their skills, teamwork, and resolve in a thrilling sports drama episode.',
        'covers/haikyu_the_dumpster_battle.jpg', 'details/haikyu_1.png', 'details/haikyu_2.png'),

       ('5 Centimeters per Second', 'Romance, Drama', 'Kenji Mizuhashi, Yoshimi Kondou', '1 hr 3 min', 7.6,
        'A poetic story divided into three segments following Takaki and Akari’s drifting relationship due to distance and time, highlighting the beauty and pain of fleeting youth and unfulfilled love.',
        'covers/5cm_per_second.jpg', 'details/5cm_per_second_1.png', 'details/5cm_per_second_2.jpg'),

       ('Kizumonogatari Part 3: Reiketsu', 'Supernatural, Mystery', 'Hiroshi Kamiya, Maaya Sakamoto', '1 hr 30 min', 7.9,
        'The final part of the Kizumonogatari trilogy focuses on Koyomi Araragi’s desperate battle to save Kiss-shot Acerola-orion Heart-under-blade and himself from dangerous enemies in this dark supernatural thriller.',
        'covers/kizumonogatari3.jpg', 'details/kizumonogatari3_1.png', 'details/kizumonogatari3_2.jpg'),

       ('Steins Gate: The Movie', 'Sci-Fi, Thriller', 'Mamoru Miyano, Asami Imai', '1 hr 30 min',
        8.2,
        'Following the events of Steins;Gate series, Okabe struggles with memories of past timelines and a mysterious girl who only he remembers, exploring themes of time travel, regret, and fate.',
        'covers/steinsgate_movie.jpg', 'details/steinsgate_movie_1.png', 'details/steinsgate_movie_2.jpg'),

       ('The Garden of Words', 'Romance, Drama', 'Miyu Irino, Kana Hanazawa', '46 min', 7.5,
        'On rainy mornings in a Tokyo park, a young shoemaker and a mysterious older woman develop a unique and delicate relationship that explores loneliness, dreams, and human connection.',
        'covers/garden_of_words.jpg', 'details/garden_of_words_1.png', 'details/garden_of_words_2.jpg'),

       ('Pokemon: The First Movie', 'Adventure, Fantasy', 'Rica Matsumoto, Ikue Otani', '1 hr 15 min', 7.1,
        'Ash and friends face Mewtwo, a powerful clone Pokémon seeking revenge against humans. The movie blends action, friendship, and ethical questions about artificial life and identity.',
        'covers/pokemon_first.jpg', 'details/pokemon_first_1.png', 'details/pokemon_first_2.jpg'),

       ('Gintama: The Very Final', 'Action, Comedy, Sci-Fi', 'Tomokazu Sugita, Daisuke Sakaguchi, Rie Kugimiya',
        '1 hr 44 min', 8.0,
        'In the ultimate battle to protect Edo, Gintoki and the Odd Jobs crew face their most dangerous enemy yet. The film balances absurd comedy with epic action and heartfelt goodbyes.',
        'covers/gintama_final.jpg', 'details/gintama_final_1.png', 'details/gintama_final_2.jpg'),

       ('Into the Forest of Fireflies', 'Drama, Romance, Fantasy', 'Kana Hanazawa, Sora Amamiya', '51 min', 7.8,
        'A quiet young man meets a mysterious forest spirit. Their brief encounter brings healing and hope in this tender supernatural romance short film.',
        'covers/forest_of_fireflies.jpg', 'details/forest_of_fireflies_1.png', 'details/forest_of_fireflies_2.jpg'),

       ('Jujutsu Kaisen 0', 'Action, Supernatural', 'Megumi Ogata, Anairis Quiñones, Allegra Clark', '1 hr 45 min', 8.3,
        'Yuta Okkotsu, cursed by a powerful spirit, enters Jujutsu High to learn control over his abilities and fight dark curses. The film acts as a prequel to the hit series, blending intense battles with character growth.',
        'covers/jujutsu_kaisen_0.jpg', 'details/jujutsu_kaisen_0_1.png', 'details/jujutsu_kaisen_0_2.jpg'),

       ('Chainsaw Man: Movie Reze', 'Action, Horror, Supernatural', 'Kensho Ono, Fairouz Ai', '2 hrs 10 min', 9.1,
        'Based on the popular manga, this movie adapts the story of Denji, a young man bonded with the chainsaw devil Pochita. It blends gory action with dark humor and emotional moments.',
        'covers/cm_reze.jpg', 'details/chainsaw_man_reze_1.png', 'details/chainsaw_man_reze_2.png');

CREATE TABLE movie_cast
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    movie_id INT,
    name     VARCHAR(100),
    role     VARCHAR(100),
    image    VARCHAR(255),
    FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE
);

INSERT INTO movie_cast (movie_id, name, role, image)
VALUES
-- 1. A Silent Voice
(1, 'Naoko Yamada', 'Director', 'cast_images/a_silent_voice/naoko_yamada.jpg'),
(1, 'Miyu Irino', 'Voice Actor', 'cast_images/a_silent_voice/miyu_irino.jpg'),
(1, 'Saori Hayami', 'Voice Actor', 'cast_images/a_silent_voice/saori_hayami.jpg'),
(1, 'Aoi Yuki', 'Voice Actor', 'cast_images/a_silent_voice/aoi_yuki.jpg'),

-- 2. Demon Slayer: Mugen Train
(2, 'Haruo Sotozaki', 'Director', 'cast_images/mugen_train/haruo_sotozaki.jpg'),
(2, 'Natsuki Hanae', 'Voice Actor', 'cast_images/mugen_train/natsuki_hanae.jpg'),
(2, 'Akari Kito', 'Voice Actor', 'cast_images/mugen_train/akari_kito.jpg'),
(2, 'Hiro Shimono', 'Voice Actor', 'cast_images/mugen_train/hiro_shimono.jpg'),

-- 3. Demon Slayer: Infinity Castle (placeholder data)
(3, 'Haruo Sotozaki', 'Director', ''),
(3, 'Natsuki Hanae', 'Voice Actor', ''),
(3, 'Akari Kito', 'Voice Actor', ''),
(3, 'Hiro Shimono', 'Voice Actor', ''),

-- 4. Fate/stay night: Heaven’s Feel III
(4, 'Tomonori Sudō', 'Director', 'cast_images/fate_hf3/tomonori_sudo.jpg'),
(4, 'Noriaki Sugiyama', 'Voice Actor', 'cast_images/fate_hf3/noriaki_sugiyama.jpg'),
(4, 'Kana Ueda', 'Voice Actor', 'cast_images/fate_hf3/kana_ueda.jpg'),
(4, 'Mai Kadowaki', 'Voice Actor', 'cast_images/fate_hf3/mai_kadowaki.jpg'),

-- 5. I Want to Eat Your Pancreas
(5, 'Shinichiro Ushijima', 'Director', 'cast_images/eat_your_pancreas/shinichiro_ushijima.jpg'),
(5, 'Mahiro Takasugi', 'Voice Actor', 'cast_images/eat_your_pancreas/mahiro_takasugi.jpg'),
(5, 'Lynn', 'Voice Actor', 'cast_images/eat_your_pancreas/lynn.jpg'),
(5, 'Yukiyo Fujii', 'Voice Actor', 'cast_images/eat_your_pancreas/yukiyo_fujii.jpg'),

-- 6. The Last: Naruto the Movie
(6, 'Tsuneo Kobayashi', 'Director', 'cast_images/last_naruto/tsuneo_kobayashi.jpg'),
(6, 'Junko Takeuchi', 'Voice Actor', 'cast_images/last_naruto/junko_takeuchi.jpg'),
(6, 'Chie Nakamura', 'Voice Actor', 'cast_images/last_naruto/chie_nakamura.jpg'),
(6, 'Noriaki Sugiyama', 'Voice Actor', 'cast_images/last_naruto/noriaki_sugiyama.jpg'),

-- 7. Violet Evergarden: The Movie
(7, 'Taichi Ishidate', 'Director', 'cast_images/violet_evergarden/taichi_ishidate.jpg'),
(7, 'Yui Ishikawa', 'Voice Actor', 'cast_images/violet_evergarden/yui_ishikawa.jpg'),
(7, 'Minako Kotobuki', 'Voice Actor', 'cast_images/violet_evergarden/minako_kotobuki.jpg'),

-- 8. Your Name
(8, 'Makoto Shinkai', 'Director', 'cast_images/your_name/makoto_shinkai.jpg'),
(8, 'Ryunosuke Kamiki', 'Voice Actor', 'cast_images/your_name/ryunosuke_kamiki.jpg'),
(8, 'Mone Kamishiraishi', 'Voice Actor', 'cast_images/your_name/mone_kamishiraishi.jpg'),

-- 9. Weathering With You
(9, 'Makoto Shinkai', 'Director', 'cast_images/weathering_with_you/makoto_shinkai.jpg'),
(9, 'Kotaro Daigo', 'Voice Actor', 'cast_images/weathering_with_you/kotaro_daigo.jpg'),
(9, 'Nana Mori', 'Voice Actor', 'cast_images/weathering_with_you/nana_mori.jpg'),

-- 10. Suzume
(10, 'Makoto Shinkai', 'Director', 'cast_images/suzume/makoto_shinkai.jpg'),
(10, 'Nanoka Hara', 'Voice Actor', 'cast_images/suzume/nanoka_hara.jpg'),
(10, 'Hokuto Matsumura', 'Voice Actor', 'cast_images/suzume/hokuto_matsumura.jpg'),

-- 11. A Whisker Away
(11, 'Junichi Sato', 'Director', 'cast_images/whisker_away/junichi_sato_tomotaka_shibayama.jpg'),
(11, 'Mirai Shida', 'Voice Actor', 'cast_images/whisker_away/mirai_shida.jpg'),
(11, 'Kento Yamazaki', 'Voice Actor', 'cast_images/whisker_away/kento_yamazaki.jpg'),

-- 12. Spirited Away
(12, 'Hayao Miyazaki', 'Director', 'cast_images/spirited_away/hayao_miyazaki.jpg'),
(12, 'Rumi Hiiragi', 'Voice Actor', 'cast_images/spirited_away/rumi_hiiragi.jpg'),
(12, 'Miyu Irino', 'Voice Actor', 'cast_images/spirited_away/miyu_irino.jpg'),

-- 13. Haikyu!!: The Dumpster Battle
(13, 'Susumu Mitsunaka', 'Director', 'cast_images/haikyu/susumu_mitsunaka.jpg'),
(13, 'Kensho Ono', 'Voice Actor', 'cast_images/haikyu/kensho_ono.jpg'),
(13, 'Kaito Ishikawa', 'Voice Actor', 'cast_images/haikyu/kaito_ishikawa.jpg'),

-- 14. 5 Centimeters per Second
(14, 'Makoto Shinkai', 'Director', 'cast_images/5cm/makoto_shinkai.jpg'),
(14, 'Kenji Mizuhashi', 'Voice Actor', 'cast_images/5cm/kenji_mizuhashi.jpg'),
(14, 'Yoshimi Kondou', 'Voice Actor', 'cast_images/5cm/yoshimi_kondou.jpg'),

-- 15. Kizumonogatari Part 3
(15, 'Tatsuya Oishi', 'Director', 'cast_images/kizumonogatari/tatsuya_oishi.jpg'),
(15, 'Hiroshi Kamiya', 'Voice Actor', 'cast_images/kizumonogatari/hiroshi_kamiya.jpg'),
(15, 'Maaya Sakamoto', 'Voice Actor', 'cast_images/kizumonogatari/maaya_sakamoto.jpg'),

-- 16. Steins;Gate: The Movie
(16, 'Kanji Wakabayashi', 'Director', 'cast_images/steinsgate/kanji_wakabayashi.jpg'),
(16, 'Mamoru Miyano', 'Voice Actor', 'cast_images/steinsgate/mamoru_miyano.jpg'),
(16, 'Asami Imai', 'Voice Actor', 'cast_images/steinsgate/asami_imai.jpg'),

-- 17. The Garden of Words
(17, 'Makoto Shinkai', 'Director', 'cast_images/garden_of_words/makoto_shinkai.jpg'),
(17, 'Miyu Irino', 'Voice Actor', 'cast_images/garden_of_words/miyu_irino.jpg'),
(17, 'Kana Hanazawa', 'Voice Actor', 'cast_images/garden_of_words/kana_hanazawa.jpg'),

-- 18. Pokémon: The First Movie
(18, 'Kunihiko Yuyama', 'Director', 'cast_images/pokemon/kunihiko_yuyama.jpg'),
(18, 'Rica Matsumoto', 'Voice Actor', 'cast_images/pokemon/rica_matsumoto.jpg'),
(18, 'Ikue Otani', 'Voice Actor', 'cast_images/pokemon/ikue_otani.jpg'),

-- 19. Gintama: The Very Final
(19, 'Chizuru Miyawaki', 'Director', 'cast_images/gintama/chizuru_miyawaki.jpg'),
(19, 'Tomokazu Sugita', 'Voice Actor', 'cast_images/gintama/tomokazu_sugita.jpg'),
(19, 'Daisuke Sakaguchi', 'Voice Actor', 'cast_images/gintama/daisuke_sakaguchi.jpg'),

-- 20. Into the Forest of Fireflies’ Light
(20, 'Takaharu Ozaki', 'Director', 'cast_images/fireflies/takaharu_ozaki.jpg'),
(20, 'Kana Hanazawa', 'Voice Actor', 'cast_images/fireflies/kana_hanazawa.jpg'),
(20, 'Sora Amamiya', 'Voice Actor', 'cast_images/fireflies/sora_amamiya.jpg'),

-- 21. Jujutsu Kaisen 0
(21, 'Sunghoo Park', 'Director', 'cast_images/jujutsu_kaisen/sunghoo_park.jpg'),
(21, 'Megumi Ogata', 'Voice Actor', 'cast_images/jujutsu_kaisen/megumi_ogata.jpg'),
(21, 'Anairis Quiñones', 'Voice Actor', 'cast_images/jujutsu_kaisen/anairis_quinones.jpg'),

-- 22. Chainsaw Man: Movie Rize (placeholders)
(22, 'Ryu Nakayama', 'Director', ''),
(22, 'Kensho Ono', 'Voice Actor', ''),
(22, 'Fairouz Ai', 'Voice Actor', '');


CREATE TABLE watchlist (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           movie_id INT NOT NULL,
                           user_id INT, -- Optional if you have multiple users
                           FOREIGN KEY (movie_id) REFERENCES movies(id)
);


