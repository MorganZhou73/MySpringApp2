dir=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
curl "http://localhost:8080/v1/users/uid01" > $dir/output.txt
echo "" >> $dir/output.txt
curl "http://localhost:8080/v1/users/uid02" >> $dir/output.txt
echo "" >> $dir/output.txt
curl "http://localhost:8080/v1/users/uid03" >> $dir/output.txt
echo "" >> $dir/output.txt

curl "http://localhost:8080/v1/users?page=1&limit=10" >> $dir/output.txt
echo "" >> $dir/output.txt

curl --request DELETE 'http://localhost:8080/v1/users/uid02' >> $dir/output.txt
echo "" >> $dir/output.txt

curl --location --request PUT 'http://localhost:8080/v1/users/uid01' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstName":"Morgan2",
    "lastName":"Zhou",
    "password":"123456"
}' >> $dir/output.txt
echo "" >> $dir/output.txt

#sed $dir/output.txt -e '1,/uid02/d' > $dir/output2.txt
