INSERT INTO sellers (id, bic, itn, psrn, business_model, company_name, corporate_account, email, external_id,
                     first_name, image_id, last_name, legal_address, payment_account, is_deleted, password, role)

VALUES (1, '1234', '4321', '9876', 'IP', 'companyName', 'corporateAccount', 'arkady1@gmail.ru',
        '44329c76-db69-425d-a5ef-f71cefec45db', 'aleks', 'url', 'zhi', 'testcity', 'cartochka', false, 'Aa4321!8ee',
        'SELLER'),
       (2, '1234', '4321', '9876', 'IP', 'companyName', 'corporateAccount', 'alex@mail.ru',
        '47678201-f3c8-4d5c-a628-2344eef50c54', 'aleks', 'url', 'zhi', 'testcity', 'cartochka', false, 'a123ddd!A',
        'SELLER'),
       (3, '1234', '4321', '9876', 'IP', 'companyName', 'corporateAccount', 'arkady@gmail.ru',
        '44329c76-db69-425d-a5ef-f71cefec44db', 'aleks', 'url', 'zhi', 'testcity', 'ACCOUNT123456', false, 'Aa4321!8ee',
        'SELLER'),
       (4, '1234', '4321', '9876', 'IP', 'companyName', 'corporateAccount', 'sellertest@gmail.ru',
        '1ae32a42-b9a9-4e6f-a403-2adae9702ae1', 'aleks', 'url', 'zhi', 'testcity', 'ACCOUNT123456', false, 'Seller!df5',
        'SELLER');
