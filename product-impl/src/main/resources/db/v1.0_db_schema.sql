USE products;

CREATE TABLE IF NOT EXISTS products(
  id                varchar, PRIMARY KEY (id)
  name              varchar,
  description       varchar,
  thumbnails        list,
  price             decimal,
  unit              varchar,
  details           list,
);
