window.initMap=function(){
const map=new google.maps.Map(document.getElementById("map"),{
    center: { lat:37.5639635, lng:127},
    zoom:12
});

const malls=[
{label:"A",name:"Coex", lat:37.5113169,lng:127.0595695},
{label:"B",name:"가든파이브", lat:37.4775778,lng:127.1249983},
{label:"C",name:"타임스퀘어", lat:37.5169933,lng:126.9035425},
{label:"D",name:"IFC몰", lat:37.5251644,lng:126.9255491},
{label:"E",name:"롯데타워몰", lat:37.5124641,lng:127.102543}
];


const bounds=new google.maps.LatLngBounds();
const infoWindow= new google.maps.InfoWindow();

malls.forEach(({label,name,lat,lng}) => {
const marker=new google.maps.Marker({
position: {lat,lng},
label,
map
});
bounds.extend(marker.position);

marker.addListener("click",() => {
map.panTo(marker.position);
infoWindow.setContent(name);
infoWindow.open({
    anchor:marker,
    map
    });
});
});
map.fitBounds(bounds);
}
