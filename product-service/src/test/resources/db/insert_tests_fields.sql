
insert into products (id, article_from_seller, article_number, count, creation_date, description,
                      external_id, is_deleted, is_visible, name, price, seller_external_id, tax_type)
values (1000001, '000000001', '3e81e74e-db56-4a38-9564-0238b5225a81', 10, '2023-02-14', 'description: 01',
        '301c5370-be41-421e-9b15-f1e80a7074f5',
        false, true, 'NAME PRODUCT 1', 1234, '301c5370-be41-421e-9b15-f1e80a7074f2', 'MAX'),

       (1000002, '000000002', '3e81e74e-db56-4a38-9564-0238b5225a82', 12, '2023-02-15', 'description: 02',
        '301c5370-be41-421e-9b15-f1e80a7074f6',
        false, true, 'NAME PRODUCT 2', 4564, '301c5370-be41-421e-9b15-f1e80a7074f2', 'MAX'),

       (1000003, '000000003', '3e81e74e-db56-4a38-9564-0238b5225a83', 13, '2023-02-16', 'description: 03',
        '301c5370-be41-421e-9b15-f1e80a7074f7',
        true, false, 'NAME PRODUCT 3', 7894, '301c5370-be41-421e-9b15-f1e80a7074f2', 'ABSENT'),

       (1000004, '000000004', '3e81e74e-db56-4a38-9564-0238b5225a84', 14, '2023-02-16', 'description: 04',
        '301c5370-be41-421e-9b15-f1e80a7074f8',
        false, true, 'NAME PRODUCT 4', 14, '301c5370-be41-421e-9b15-f1e80a7074f2', 'MIN'),

       (1000010, '000000005', '3e81e74e-db56-4a38-9564-0238b5225a85', 15, '2023-02-16', 'description: 05',
        '301c5370-be41-421e-9b15-f1e80a7074b1',
        true, false, 'NAME PRODUCT 5', 104, '301c5370-be41-421e-9b15-f1e80a7074a1', 'MIN'),

       (1000011, '000000006', '3e81e74e-db56-4a38-9564-0238b5225a86', 16, '2023-02-16', 'description: 06',
        '301c5370-be41-421e-9b15-f1e80a7074b2',
        false, true, 'NAME PRODUCT 6', 14, '301c5370-be41-421e-9b15-f1e80a7074a1', 'MAX'),

       (1000012, '000000007', '3e81e74e-db56-4a38-9564-0238b5225a87', 17, '2023-02-16', 'description: 07',
        '301c5370-be41-421e-9b15-f1e80a7074b3',
        true, false, 'NAME PRODUCT 7', 14, '301c5370-be41-421e-9b15-f1e80a7074a1', 'MIN'),

       (1000013, '000000008', '3e81e74e-db56-4a38-9564-0238b5225a88', 18, '2023-02-16', 'new description: 08',
        '301c5370-be41-421e-9b15-f1e80a7074b4',
        false, true, 'NAME PRODUCT 8', 14, '301c5370-be41-421e-9b15-f1e80a7074a1', 'MIN'),

       (1000051, '000000001', '3e81e74e-db56-4a38-9564-0238b5225a71', 10, '2023-02-14', 'description: 01',
        '301c5370-be41-421e-9b15-f1e80a7074d1',
        true, false, 'NAME PRODUCT DELETE 1', 1234, '301c5370-be41-421e-9b15-f1e80a7071d1', 'MAX'),

       (1000052, '000000002', '3e81e74e-db56-4a38-9564-0238b5225a72', 12, '2023-02-15', 'description: 02',
        '301c5370-be41-421e-9b15-f1e80a7074d2',
        true, false, 'NAME PRODUCT DELETE 2', 4564, '301c5370-be41-421e-9b15-f1e80a7071d1', 'MAX'),

       (1000053, '000000003', '3e81e74e-db56-4a38-9564-0238b5225a73', 13, '2023-02-16', 'description: 03',
        '301c5370-be41-421e-9b15-f1e80a7074d3',
        true, false, 'NAME PRODUCT DELETE 3', 7894, '301c5370-be41-421e-9b15-f1e80a7071d1', 'ABSENT'),

       (1000054, '000000004', '3e81e74e-db56-4a38-9564-0238b5225a74', 14, '2023-02-16', 'description: 04',
        '301c5370-be41-421e-9b15-f1e80a7074d4',
        true, false, 'NAME PRODUCT DELETE 4', 14, '301c5370-be41-421e-9b15-f1e80a7071d1', 'MIN'),

       (1000060, '000000006', '3e81e74e-db56-4a38-9564-0238b5225a75', 15, '2023-02-16', 'description: 05',
        '301c5370-be41-421e-9b15-f1e80a7074d5',
        false, false, 'NAME PRODUCT DELETE 5', 104, '301c5370-be41-421e-9b15-f1e80a7071d1', 'MIN'),

       (1000061, '000000006', '3e81e74e-db56-4a38-9564-0238b5225a76', 16, '2023-02-16', 'description: 06',
        '301c5370-be41-421e-9b15-f1e80a7074d6',
        false, false, 'NAME PRODUCT DELETE 6', 14, '301c5370-be41-421e-9b15-f1e80a7071d1', 'MAX'),

       (1000062, '000000007', '3e81e74e-db56-4a38-9564-0238b5225a77', 17, '2023-02-16', 'description: 07',
        '301c5370-be41-421e-9b15-f1e80a7074d7',
        true, false, 'NAME PRODUCT DELETE 7', 14, '301c5370-be41-421e-9b15-f1e80a7071d1', 'MIN'),

       (1000063, '000000008', '3e81e74e-db56-4a38-9564-0238b5225a78', 18, '2023-02-16', 'new description: 08',
        '301c5370-be41-421e-9b15-f1e80a7074d8',
        false, false, 'NAME PRODUCT DELETE 8', 14, '301c5370-be41-421e-9b15-f1e80a7071d1', 'MIN'),

       (1000064, '000000008', '37678201-f3c8-4d5c-a628-2344eef50c62', 18, '2023-02-16', 'new description: 08',
        '37678201-f3c8-4d5c-a628-2344eef50c61',
        false, false, 'NAME PRODUCT', 14, '37678201-f3c8-4d5c-a628-2344eef50c60', 'MIN');;