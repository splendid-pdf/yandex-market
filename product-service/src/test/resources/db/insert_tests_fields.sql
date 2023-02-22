    insert into products (id, article_from_seller, article_number, count, creation_date, description,
                          external_id, is_deleted, is_visible, name, price, seller_external_id, tax_type)
    values (1000001, '000000001', '99001101', 10, '2023-02-14', 'description: 01',
            '301c5370-be41-421e-9b15-f1e80a7074f5',
            false, true, 'NAME PRODUCT 1', 1234, '301c5370-be41-421e-9b15-f1e80a7074f2', 'MAX'),

           (1000002, '000000002', '99001102', 12, '2023-02-15', 'description: 02',
            '301c5370-be41-421e-9b15-f1e80a7074f6',
            false, true, 'NAME PRODUCT 2', 4564, '301c5370-be41-421e-9b15-f1e80a7074f2', 'MAX'),

           (1000003, '000000003', '99001103', 13, '2023-02-16', 'description: 03',
            '301c5370-be41-421e-9b15-f1e80a7074f7',
            true, false, 'NAME PRODUCT 3', 7894, '301c5370-be41-421e-9b15-f1e80a7074f2', 'ABSENT'),

           (1000004, '000000004', '99001104', 14, '2023-02-16', 'description: 04',
            '301c5370-be41-421e-9b15-f1e80a7074f8',
            false, true, 'NAME PRODUCT 4', 14, '301c5370-be41-421e-9b15-f1e80a7074f2', 'MIN'),

           (1000010, '000000005', '99001105', 15, '2023-02-16', 'description: 05',
            '301c5370-be41-421e-9b15-f1e80a7074b1',
            true, false, 'NAME PRODUCT 5', 104, '301c5370-be41-421e-9b15-f1e80a7074a1', 'MIN'),

           (1000011, '000000006', '99001106', 16, '2023-02-16', 'description: 06',
            '301c5370-be41-421e-9b15-f1e80a7074b2',
            false, true, 'NAME PRODUCT 6', 14, '301c5370-be41-421e-9b15-f1e80a7074a1', 'MAX'),

            (1000012, '000000007', '99001107', 17, '2023-02-16', 'description: 07',
            '301c5370-be41-421e-9b15-f1e80a7074b3',
            true, false, 'NAME PRODUCT 7', 14, '301c5370-be41-421e-9b15-f1e80a7074a1', 'MIN'),

            (1000013, '000000008', '99001108', 18, '2023-02-16', 'new description: 08',
            '301c5370-be41-421e-9b15-f1e80a7074b4',
            false, true, 'NAME PRODUCT 8', 14, '301c5370-be41-421e-9b15-f1e80a7074a1', 'MIN');