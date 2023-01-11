truncate shop_systems,
    delivery_intervals,
    delivery_zones,
    pickup_point_partners,
    deliveries,
    branches,
    special_offers,
    shop_systems
    cascade;

-- DO
-- $$
--     DECLARE
--         i TEXT;
--     BEGIN
--         FOR i IN (select sequence_name from information_schema.sequences)
--             LOOP
--                 EXECUTE 'alter sequence '''|| i ||''' restart with 2' ;
--             END LOOP;
--     END
-- $$;
