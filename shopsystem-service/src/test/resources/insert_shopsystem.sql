truncate shop_systems cascade;

alter sequence shop_sequence restart with 2;
alter sequence branch_sequence restart with 2;
alter sequence delivery_interval_sequence restart with 3;
alter sequence delivery_zone_sequence restart with 3;

insert into shop_systems(id,
                         external_id,
                         name,
                         token,
                         number,
                         email,
                         country,
                         region,
                         city,
                         street,
                         house_number,
                         office_number,
                         postcode,
                         logo_url,
                         rating,
                         is_disabled,
                         is_test,

--          todo shopsystem hasn't coordinates
                         latitude,
                         longitude)
values (1,
        '87ba7a03-054e-4c7f-ac35-f4802d66cec3',
        'Eldorado',
        'DzdWIiOiIxMjM0NTY3ODkwIiwibmFtNTE2MjM5MDIyfQ',
        '8(937)640-9999',
        'eldorado@sup.com',
        'Russia',
        'Moscow Region',
        'Moscow',
        'Krizhanovskaya',
        '5B',
        '12C',
        '117218',
        'https://static.eldorado.ru/espa/l.42.0-ab-esp-5648.3-Ps0BSwVDAKQkPjSPyBVlu/static_spa/assets/logo.dc65dadd.svg',
        4.8,
        false,
        false,
        54,
        26);

insert into branches (id,
                      external_id,
                      shop_system_id,
                      name,
                      token,
                      ogrn,

--                  todo  Branch location hasn't country and region
                      country,
                      region,
--
                      city,
                      street,
                      house_number,
                      office_number,
                      postcode,
                      latitude,
                      longitude,
                      hotline_phone,
                      service_phone,
                      email,
                      pickup,
                      is_disabled)
values (1,
        '6166b912-878c-11ed-a1eb-0242ac120002',
        1,
        'Eldorado Ohta Moll',
        '0d305bfS05e74beS8fe9cedfaf4f6003',
        '1227700339492',
        'Russia',
        'Moscow Region',
        'Saint-Petersburg',
        'Brantovskaya d.',
        '5B',
        '12C',
        '117218',
        59.934542,
        30.416842,
        '8(937)640-9999',
        '8(939)640-8888',
        'eldorado-ohta@eldorado.ru',
        true,
        false);

insert into special_offers (id,
                            shop_system_id,
                            name,
                            type,
                            value,
                            terms)
values (1,
        1,
        'Подарок на день рождение',
        'DISCOUNT',
        10,
        'Скидка предоставляется в течение 7 дней до дня рождения и 7 дней после');

insert into deliveries (id,
                        branch_id,
                        has_delivery,
                        has_delivery_to_pickup_point,
                        has_express_delivery)
values (1,
        1,
        true,
        true,
        true);

insert into pickup_point_partners (pickup_partner_id,
                                   pickup_point_partners)
values (1, 'YANDEX_MARKET'),
       (1, 'OZON'),
       (1, 'WILDBERRIES');


insert into delivery_zones (id,
                            zone_id,
                            delivery_id,
                            radius_in_meters,
                            standard_delivery_price,
                            express_delivery_price)
values (1,
        'id742100',
        1,
        2000,
        399,
        599),
--
       (2,
        'id742101',
        1,
        5000,
        799,
        599);

insert into delivery_intervals (id,
                                delivery_id,
                                interval_id,
                                period_start,
                                period_end)
values (1,
        1,
        '126600302',
        '10:00',
        '12:00'),
--
       (2,
        1,
        '126600303',
        '12:00',
        '18:00');
