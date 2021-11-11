create table if not exists food(
    id serial primary key,
    name text,
    weightGrams int,
    calories int,
    fat int,
    saturatedFat int,
    cholesterol int,
    sodium int,
    carbohydrate int,
    dietaryFiber int,
    sugars int,
    protein int,
    potassium int
);