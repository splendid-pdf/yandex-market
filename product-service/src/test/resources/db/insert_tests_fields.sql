insert into products (id, article_number, description, height, length, width, external_id, image_url, is_deleted,
                      is_visible, manufacturer, name, rating, weight)
values (9990, 10000090, 'DESC DESC DESC DESC DESC DESC DESC ', '90.75', '40.45', '15.5',
        '9f1d3971-9106-40b5-9b18-2014c0b703f0', 'http://marketplace.com/image1.png', false, false,
        'MANU TEST 1', 'Test RTX product 1', 4.5, '15.4'),
       (9991, 10000091, 'DESC 2 DESC 2 DESC 2 DESC 2 DESC 2 DESC 2 DESC 2 ', '90.76', '40.46', '16.6',
        '9f1d3971-9106-40b5-9b18-2014c0b703f1', 'http://marketplace.com/image2.png', false, false,
        'MANU TEST 2', 'Test RTX product 2',  4.4, '15.4'),
       (9992, 10000092, 'DESC 3 DESC 3 DESC 3 DESC 3 DESC 3 DESC 3 DESC 3 ', '90.77', '40.47', '17.7',
        '9f1d3971-9106-40b5-9b18-2014c0b703f2', 'http://marketplace.com/image3.png', false, false,
        'MANU TEST 3', 'Test RTX product 3', 4.3, '15.4');

insert into product_prices (id, branch_id, discounted_price, external_id, price, product_id, shop_system_id)
values (990, '9f1d3971-9106-40b5-9b18-2014c0b703b0', 10.1, 'f1ea093c-daa2-43a4-b61c-fb8596034e70', 101.99,
        '9f1d3971-9106-40b5-9b18-2014c0b703f0', 'caa2a172-5f4c-4224-95ce-c79a89684e90'),
       (991, '9f1d3971-9106-40b5-9b18-2014c0b703b1', 10.1, 'f1ea093c-daa2-43a4-b61c-fb8596034e71', 102.99,
        '9f1d3971-9106-40b5-9b18-2014c0b703f1', 'caa2a172-5f4c-4224-95ce-c79a89684e91'),
       (992, '9f1d3971-9106-40b5-9b18-2014c0b703b0', 10.1, 'f1ea093c-daa2-43a4-b61c-fb8596034e72', 103.99,
        '9f1d3971-9106-40b5-9b18-2014c0b703f0', 'caa2a172-5f4c-4224-95ce-c79a89684e90'),
       (993, '9f1d3971-9106-40b5-9b18-2014c0b703b3', 10.1, 'f1ea093c-daa2-43a4-b61c-fb8596034e73', 104.99,
        '9f1d3971-9106-40b5-9b18-2014c0b703f3', 'caa2a172-5f4c-4224-95ce-c79a89684e90');