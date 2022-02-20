Array = csvread('Data.csv', 1, 0);
col1 = Array(:, 1);
col2 = Array(:, 2);
col3 = Array(:, 5);
plot3(col1, col2, col3);
grid on;
xlabel("Year");
ylabel("country id");
zlabel("Number of medals")