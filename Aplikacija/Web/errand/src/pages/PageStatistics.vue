<template>
    <Spinner v-if="statistics == null || services == null" />
    <div class="statistics-wrapper" v-else>
        <div class="statistics-row">
            <div class="statistics-item brojKorisnika odlepi" >
                <countTo :startVal='0' :endVal='statistics.number_of_users' :duration='3000' :separator="'.'" class="brojka"></countTo>
                <span v-if="isSerbian"> Ukupan broj korisnika </span>
                <span v-else> Total number of users </span>
            </div>
            <div class="statistics-item">
                <VueApexCharts type="pie" width="380" height="200" :options="pieChart.chartOptions" :series="pieChart.series"></VueApexCharts>
            </div>
        </div> 
        <div class="statistics-row">
            <div class="statistics-item odlepi" >
                <VueApexCharts type="line" height="350" width="500" :options="newUsers.chartOptions" :series="newUsers.series"></VueApexCharts>
            </div>
            <div class="statistics-item">
                <VueApexCharts type="line" height="350" width="500" :options="createdRequests.chartOptions" :series="createdRequests.series"></VueApexCharts>
            </div>
        </div>
        <div class="statistics-row">
            <div class="statistics-item odlepi" >
                <VueApexCharts type="bar" height="350" width="500" :options="tasksPerService.chartOptions" :series="tasksPerService.series"></VueApexCharts>
            </div>
            <div class="statistics-item">
                <VueApexCharts type="bar" height="350" width="500" :options="usersPerService.chartOptions" :series="usersPerService.series"></VueApexCharts>
            </div>
        </div>
        <div class="statistics-row">
            <div class="statistics-item odlepi" >
                <VueApexCharts type="bar" height="350" width="500" :options="userAchievement.chartOptions" :series="userAchievement.series"></VueApexCharts>
            </div>
            <div class="statistics-item">
                <VueApexCharts type="bar" height="350" width="500" :options="userRatings.chartOptions" :series="userRatings.series"></VueApexCharts>
            </div>
        </div>      
    </div>
</template>

<script>
import countTo from 'vue-count-to'
import VueApexCharts from 'vue-apexcharts'
import Spinner from "@/components/Spinner"
export default {
    components:
    {
        countTo,
        Spinner,
        VueApexCharts
    },
    computed:
    {
        isSerbian()
        {
            return this.$store.state.isSerbian
        },
        statistics()
        {
            return this.$store.state.statistics
        },
        services()
        {
            return this.$store.state.allServices
        },
        pieLabels()
        {
            if(this.isSerbian)
                return ["Uspešno završeni zahtevi", "Otkazani zahtevi"]
            else
                return ["Successfully completed requests", "Canceled requests"]
        },
        newUsersName()
        {
            if(this.isSerbian)
                return "Broj novih korisnika"
            else
                return "Number of new users"
        },
        spisakServisa()
        {
            var spisak = []
            if(this.isSerbian)
            {
                this.services.forEach(element =>
                {
                    spisak.push(element.service_type_sr)
                })
            }
            else
            {
                this.services.forEach(element =>
                {
                    spisak.push(element.service_type_en)
                })
            }
            return spisak
        },
        pieChart()
        {
            return{
                series: this.statistics.finished_requests,
                chartOptions: 
                {
                    chart: 
                    {
                        width: 380,
                        type: 'pie',
                    },
                    labels: this.pieLabels,
                    responsive: 
                    [{
                        breakpoint: 480,
                        options: 
                        {
                            chart: 
                            {
                                width: 300
                            },
                            legend: 
                            {
                                position: 'bottom'
                            }
                        }
                    }],
                    colors: ['#50C878', '#ED2939']
                }
            }
        },
        newUsers()
        {
            return{
                series: [{
                    name: this.newUsersName,
                    data: this.statistics.new_users
                }],
                chartOptions: 
                {
                    chart: 
                    {
                        height: 350,
                        width: 500,
                        type: 'bar',
                    },
                    colors: ["#680101"],
                    plotOptions: 
                    {
                        bar: 
                        {
                            dataLabels: 
                            {
                                position: 'top', // top, center, bottom
                            },
                        }
                    },
                    dataLabels: 
                    {
                        enabled: false,
                        formatter: function (val) {
                            return val;
                        },
                        offsetY: -20,
                        style: {
                            fontSize: '12px',
                            colors: ["#304758"]
                        }
                    },
                    responsive: 
                    [{
                        breakpoint: 570,
                        options: 
                        {
                            chart: 
                            {
                                width: 280
                            }
                        }
                    }],
                    xaxis: 
                    {
                        categories: (this.isSerbian?["Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Avg", "Sep", "Okt", "Nov", "Dec"]:["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]),
                        position: 'top',
                        axisBorder: {
                            show: false
                        },
                        axisTicks: {
                            show: false
                        },
                        crosshairs: {
                            fill: {
                                type: 'gradient',
                                gradient: {
                                    colorFrom: '#D8E3F0',
                                    colorTo: '#BED1E6',
                                    stops: [0, 100],
                                    opacityFrom: 0.4,
                                    opacityTo: 0.5,
                                }
                            }
                        },
                        tooltip: {
                            enabled: true,
                        }
                    },
                    yaxis: {
                        axisBorder: {
                            show: false
                        },
                        axisTicks: {
                            show: false,
                        },
                        labels: {
                            show: false,
                            formatter: function (val) {
                            return val;
                            }
                        }
                        
                    },
                    title: {
                        text: (this.isSerbian? "Broj novih korisnika po mesecima" : "Number of new users per month"),
                        floating: true,
                        offsetY: 330,
                        align: 'center',
                        style: {
                            color: '#444'
                        }
                    }
                }
            }
        },
        createdRequests()
        {
            return{
                series: [{
                    name: (this.isSerbian? "Broj kreiranih zahteva": "Number of created requests"),
                    data: this.statistics.requests_created
                }],
                chartOptions: 
                {
                    chart: 
                    {
                        height: 350,
                        width: 500,
                        type: 'bar',
                    },
                    colors: ["#4CBB17"],
                    plotOptions: 
                    {
                        bar: 
                        {
                            dataLabels: 
                            {
                                position: 'top', // top, center, bottom
                            },
                        }
                    },
                    dataLabels: 
                    {
                        enabled: false,
                        formatter: function (val) {
                            return val;
                        },
                        offsetY: -20,
                        style: {
                            fontSize: '12px',
                            colors: ["#304758"]
                        }
                    },
                    responsive: 
                    [{
                        breakpoint: 570,
                        options: 
                        {
                            chart: 
                            {
                                width: 280
                            }
                        }
                    }],
                    xaxis: 
                    {
                        categories: (this.isSerbian?["Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Avg", "Sep", "Okt", "Nov", "Dec"]:["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]),
                        position: 'top',
                        axisBorder: {
                            show: false
                        },
                        axisTicks: {
                            show: false
                        },
                        crosshairs: {
                            fill: {
                                type: 'gradient',
                                gradient: {
                                    colorFrom: '#D8E3F0',
                                    colorTo: '#BED1E6',
                                    stops: [0, 100],
                                    opacityFrom: 0.4,
                                    opacityTo: 0.5,
                                }
                            }
                        },
                        tooltip: {
                            enabled: true,
                        }
                    },
                    yaxis: {
                        axisBorder: {
                            show: false
                        },
                        axisTicks: {
                            show: false,
                        },
                        labels: {
                            show: false,
                            formatter: function (val) {
                            return val;
                            }
                        }
                        
                    },
                    title: {
                        text: (this.isSerbian? "Broj kreiranih zahteva po mesecima" : "Number of created requests per month"),
                        floating: true,
                        offsetY: 330,
                        align: 'center',
                        style: {
                            color: '#444'
                        }
                    }
                }
            }
        },
        tasksPerService()
        {
            return{
                series: [{
                    name: (this.isSerbian? "Broj zadataka u kojima se javlja": "Number tasks in wich it occurs"),
                    data: this.statistics.tasks_per_service
                }],
                chartOptions: 
                {
                    chart: 
                    {
                        height: 350,
                        width: 500,
                        type: 'bar',
                    },
                    colors: ["#59A5D8"],
                    plotOptions: 
                    {
                        bar: 
                        {
                            horizontal: true,
                        }
                    },
                    dataLabels: 
                    {
                        enabled: false
                    },
                    responsive: 
                    [{
                        breakpoint: 570,
                        options: 
                        {
                            chart: 
                            {
                                width: 280
                            }
                        }
                    }],
                    xaxis: {
                        categories: this.spisakServisa,
                    },
                    title: {
                        text: (this.isSerbian? "Broj zadataka u kojima se javalja svaka od usluga" : "Number of tasks in which each sevice occurs"),
                        floating: true,
                        offsetY: 330,
                        align: 'center',
                        style: {
                            color: '#444'
                        }
                    }
                }
            }
        },
        usersPerService()
        {
            return{
                series: [{
                    name: (this.isSerbian? "Broj korisnika koji je izvršavaju": "Number of users who offer it"),
                    data: this.statistics.tasks_per_service
                }],
                chartOptions: 
                {
                    chart: 
                    {
                        height: 350,
                        width: 500,
                        type: 'bar',
                    },
                    colors: ["#FFA50F"],
                    plotOptions: 
                    {
                        bar: 
                        {
                            horizontal: true,
                        }
                    },
                    dataLabels: 
                    {
                        enabled: false
                    },
                    responsive: 
                    [{
                        breakpoint: 570,
                        options: 
                        {
                            chart: 
                            {
                                width: 280
                            }
                        }
                    }],
                    xaxis: {
                        categories: this.spisakServisa,
                    },
                    title: {
                        text: (this.isSerbian? "Broj korisnika koji izvršavaju svaku uslugu" : "Number of users who offer each service"),
                        floating: true,
                        offsetY: 330,
                        align: 'center',
                        style: {
                            color: '#444'
                        }
                    }
                }
            }
        },
        userAchievement()
        {
            return{
                series: [{
                    name: (this.isSerbian? "Broj korisnika": "Number of users"),
                    data: this.statistics.achievements_distribution[1]
                }],
                chartOptions: 
                {
                    chart: 
                    {
                        height: 350,
                        width: 500,
                        type: 'line',
                    },
                    colors: ["#E8C00F"],
                    stroke: {
                        curve: 'stepline',
                    },
                    dataLabels: 
                    {
                        enabled: false
                    },
                    responsive: 
                    [{
                        breakpoint: 570,
                        options: 
                        {
                            chart: 
                            {
                                width: 280
                            }
                        }
                    }],
                    title: {
                        text: (this.isSerbian? "Raspodela korisnika po broju dostignuća koja imaju" : "Distribution of users per number of achievements they have"),
                        align: 'left'
                    },
                    markers: {
                        hover: {
                            sizeOffset: 4
                        }
                    },
                    xaxis: 
                    {
                        categories: ["0-5", "5-10", "10-15", "15-20", "20-25", ">25"],
                        position: 'top',
                        axisBorder: {
                            show: false
                        },
                        axisTicks: {
                            show: false
                        },
                        crosshairs: {
                            fill: {
                                type: 'gradient',
                                gradient: {
                                    colorFrom: '#D8E3F0',
                                    colorTo: '#BED1E6',
                                    stops: [0, 100],
                                    opacityFrom: 0.4,
                                    opacityTo: 0.5,
                                }
                            }
                        },
                        tooltip: {
                            enabled: true,
                        }
                    },
                }
            }
        },
        userRatings()
        {
            return{
                series: [{
                    name: (this.isSerbian? "Broj korisnika": "Number of users"),
                    data: this.statistics.users_per_grade
                }],
                chartOptions: 
                {
                    chart: 
                    {
                        height: 350,
                        width: 500,
                        type: 'line',
                    },
                    colors: ["#5E2D79"],
                    stroke: {
                        curve: 'stepline',
                    },
                    dataLabels: 
                    {
                        enabled: false
                    },
                    responsive: 
                    [{
                        breakpoint: 570,
                        options: 
                        {
                            chart: 
                            {
                                width: 280
                            }
                        }
                    }],
                    title: {
                        text: (this.isSerbian? "Raspodela korisnika po prosečnoj oceni" : "Distribution of users depending on their average grade"),
                        align: 'left'
                    },
                    markers: {
                        hover: {
                            sizeOffset: 4
                        }
                    },
                    xaxis: 
                    {
                        categories: ["1-1.5", "1.5-2.5", "2.5-3.5", "3.5-4.5", "4.5-5"],
                        position: 'bottom',
                        axisBorder: {
                            show: false
                        },
                        axisTicks: {
                            show: false
                        },
                        crosshairs: {
                            fill: {
                                type: 'gradient',
                                gradient: {
                                    colorFrom: '#D8E3F0',
                                    colorTo: '#BED1E6',
                                    stops: [0, 100],
                                    opacityFrom: 0.4,
                                    opacityTo: 0.5,
                                }
                            }
                        },
                        tooltip: {
                            enabled: true,
                        }
                    },
                }
            }
        },
    },
    created()
    {
        if(this.$store.state.statistics == null)
            this.$store.dispatch("getStatistics")
        if(this.$store.state.allServices == null)
            this.$store.dispatch("fillServices")
    }
}
</script>

<style scoped>
    .statistics-wrapper
    {
        display:flex;
        flex-direction: column;
        align-items: center;
        width:100%;
        padding-top:30px;
        padding-bottom:30px;
    }

    .statistics-row
    {
        display: flex;
        flex-direction: row;
        align-items: center;
        margin-bottom: 30px;
    }

    .statistics-item
    {
        display: flex;
        flex-direction: column;
        align-items: center;
        background-color: white;
        border-radius: 30px;
        padding:20px;
        font-size: 22px;
        font-weight: 600;
    }

    .odlepi
    {
        margin-right:20px;
    }

    .brojKorisnika
    {
        width:340px;
    }

    .brojka
    {
        font-size: 120px;
        font-weight:600;
    }

    @media only screen and (max-width:1135px)
    {
        .statistics-row
        {
            flex-direction: column;
            margin-bottom: 0px;
        }

        .statistics-item
        {
            margin-bottom:20px;
        }

        .odlepi
        {
            margin-right: 0px;
        }
    }
</style>