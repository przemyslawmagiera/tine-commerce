insert into Category values
  (-100, 'root', 'root Category', 'Main category [root]'),
  (-101, 'cat1', 'Wszystkie ubrania', 'Odzież'),
  (-102, 'cat2', '', 'Obuwie'),
  (-103, 'cat3', '', 'Sport'),
  (-104, 'obuwieEle', 'obuwieEle', 'Eleganckie'),
  (-105, 'obuwieTramp', '', 'Trampki'),
  (-106, 'obuwieLetnie', '', 'Letnie'),
  (-107, 'odzKoszulki', '', 'Koszulki'),
  (-108, 'odzBluzy', '', 'Bluzy'),
  (-109, 'odzSpodnie', '', 'Spodnie'),
  (-110, 'spSprzet', '', 'Sprzęt'),
  (-111, 'sportObuwie', '', 'Obuwie'),
  (-112, 'sportSprzHantl', '', 'Hantle'),
  (-113, 'sportSprzNar', '', 'Narty');

insert into Category_Category values
  (-101, -100),
  (-102, -100),
  (-103, -100),
  (-104, -101),
  (-105, -101),
  (-106, -101),
  (-107, -102),
  (-108, -102),
  (-109, -102),
  (-110, -103),
  (-111, -103),
  (-112, -110),
  (-113, -110);

insert into Category_feature values
  (-100, 'rozmiarObuwie_t', '', 'Rozmiar', true, false, 'ENUM'),
  (-101, 'rozmiarOdziez_t', '', 'Rozmiar', true, false, 'INTEGER'),
  (-102, 'kolor_t', '', 'Kolor', true, false, 'STRING'),
  (-103, 'material_t', '', 'Materiał', true, false, 'STRING'),
  (-104, 'marka_t', '', 'Marka', true, true, 'STRING'),
  (-105, 'dlugoscSpodnie_t', '', 'Długość', true, false, 'ENUM'),
  (-106, 'podszewkaObuwie_t', '', 'Podszewka', true, false, 'ENUM');


insert into category_feature_assignment values
  (-100, 'rootMarka', -100, -104),
  (-101, 'obuwRozm', -102, -100),
  (-102, 'odziezRozm', -101, -101),
  (-103, 'obuwKolor', -102, -102),
  (-104, 'odziezKolor', -101, -102),
  (-105, 'material', -101, -103),
  (-106, 'podszObuwie', -102, -106),
  (-107, 'dlugoscSpodnie', -109, -105);

insert into Category_feature_value values
  (-101, 'rozmS', 'S', -101),
  (-102, 'rozmM', 'M', -101),
  (-103, 'rozmL', 'L', -101),
  (-104, 'rozmXL', 'XL', -101),
  (-105, 'spodnieKrot', 'Krótkie', -105),
  (-106, 'spodDlugie', 'Długie', -105),
  (-107, 'podszewkaSkora', 'Skóra', -106),
  (-108, 'podszewkaMaterial', 'Materiał', -106),
  (-109, 'podszewkaWelna', 'Wełna', -106);

insert into Product values
  (-100, '43243545', 'Koszulka polo Tommy Hilfiger, jest idealną propozycją dla mężczyzn ceniących sobie elegancję a zarazem wygodę i styl.', 'Koszulka polo Hilfiger slim'),
  (-101, '43546576', 'Koszulka polo Lacoste, jest idealną propozycją dla mężczyzn ceniących sobie elegancję a zarazem wygodę i styl.', 'Koszulka polo Lacoste'),
  (-102, '45457878', 'Koszulka polo Nike, jest idealną propozycją dla mężczyzn ceniących sobie elegancję a zarazem wygodę i styl.', 'Koszulka polo wyjściowa'),
  (-103, '48457878', 'Spodnie Wrangler, jest idealną propozycją dla mężczyzn ceniących sobie elegancję a zarazem wygodę i styl.', 'Spodnie slim Wrangler'),
  (-104, '48057878', 'Spodnie Levis, jest idealną propozycją dla mężczyzn ceniących sobie elegancję a zarazem wygodę i styl.', 'Spodnie krótkie classic Levis'),
  (-105, '48051878', 'Buty cichobiegi, jest idealną propozycją dla mężczyzn ceniących sobie elegancję a zarazem wygodę i styl.', 'Trampki vans classic');

insert into Product_feature values
  (-100, 'pf1', 'XL', -101, -100),
  (-101, 'pf2', 'L', -101, -101),
  (-102, 'pf3', 'S', -101, -102),
  (-103, 'pf4', 'Hilfiger', -100, -100),
  (-104, 'pf5', 'Lacoste', -100, -101),
  (-105, 'pf6', 'Nike', -100, -102);

insert into Product_category values
  (-107,-100),
  (-107,-101),
  (-107,-102),
  (-109,-103),
  (-109,-104),
  (-105,-105);


insert into Price values
  (-100, 'kod1', 249.90, 'PLN',-100),
  (-101, 'kod2', 229.90, 'PLN',-101),
  (-102, 'kod3', 149.00, 'PLN',-102),
  (-103, 'kod4', 349.90, 'PLN',-102),
  (-104, 'kod5', 299.90, 'PLN',-102),
  (-105, 'kod6', 179.90, 'PLN',-102);
  
  
insert into search_field values 
  (1,'kod11',TRUE,'category',TRUE),
  (2,'dcds',FALSE,'name',TRUE),
  (3,'cds',FALSE,'description',FALSE),
  (4,'kod1',FALSE,'photos',FALSE);


