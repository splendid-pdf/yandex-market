insert into products (id, article_from_seller, article_number, count, creation_date, description,
                      external_id, is_deleted, is_visible, name, price, seller_external_id, tax_type)
values (1000001, '123123123', '301c5370-be41-421e-9b15-f1e80a7074f5', 123, '2023-02-14', '123123123123123123123',
        '301c5370-be41-421e-9b15-f1e80a7074f5',
        false, true, 'NAME PRODUCT 123', 1234, '301c5370-be41-421e-9b15-f1e80a7074f2', 'MAX'),
       (1000002, '456456456', '301c5370-be41-421e-9b15-f1e80a7074f5', 456, '2023-02-15', '456456456456456456456',
        '301c5370-be41-421e-9b15-f1e80a7074f6',
        false, true, 'NAME PRODUCT 456', 4564, '301c5370-be41-421e-9b15-f1e80a7074f2', 'MAX'),
       (1000003, '789789789', '301c5370-be41-421e-9b15-f1e80a7074f5', 789, '2023-02-16', '789789789789789789789',
        '301c5370-be41-421e-9b15-f1e80a7074f7',
        false, true, 'NAME PRODUCT 789', 7894, '301c5370-be41-421e-9b15-f1e80a7074f2', 'MIN');