A native app that shows Jake Wharton Github repositories as list. It use pagination (request 15 items per request). When the user reaches the third item from the bottom, it request another batch. During the request execution you can see a progress bar as last item in the list. If there is no more items available, you will not see the progessbar at bootom and message will apper "Data not Available".

App Status - 
* Internet available - get the data from the server
* Internet Unavailable & offline data present - get the offline data and add to the adapter
* No Internet & No Offline data - close the app and display the message.
              
Note : when the app connect to the internet, It will delete all the previous data.
      
