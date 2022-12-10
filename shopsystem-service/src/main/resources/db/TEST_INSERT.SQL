insert into shop_systems (id, external_id, is_disabled, is_test, city, country, house_number, latitude, longitude,
                          office_number, postcode, region, street, logo_url, name, rating, email, number, token)
values (999, 'cccccccc-cccc-cccc-cccc-cccccccccccc', false, false, 'Saratov', 'Russia',
        12, 51.32, 46.00, 4, 410080, 'Saratov region',
        'Leninskaya', 'https:\\random.image.jpg', 'DNS', 4.3, 'support.main@dns.ru',
        '640-640', '7fa-ff55df4f-gadf');

select * from shop_systems;