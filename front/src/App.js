import React, {useState, useEffect} from 'react';
import './App.css';
import Chart from 'chart.js/auto';
import {Bar} from 'react-chartjs-2';
import {sortBy} from 'lodash';

function App() {
    const initData = {
        labels: [],
        datasets: [
            {
                label: '',
                data: [],
                backgroundColor: [],
                borderWidth: 1,
            }
        ]
    }
    useEffect(() => {
        const fetchMonths = async () => {
            const res = await fetch('/mails/MailsPerMonth')

            let dataSet = await res.json()
            dataSet = sortBy(sortBy(dataSet, ["_id.month"]), ["_id.year"]);
            let maxSum = Math.max.apply(Math, dataSet.map(function(o) {
                return o.sum; }));
            console.log(maxSum)
            setCharData({
                labels: dataSet.map((entry) => entry._id.month.toString() + "-" + entry._id.year.toString()),
                datasets: [
                    {
                        label: "Number of mails per month",
                        data: dataSet.map((entry) => entry.sum),
                        backgroundColor: (ctx) => (ctx.dataset.data[ctx.dataIndex] > (maxSum-maxSum/3) ? 'red' : ctx.dataset.data[ctx.dataIndex] > (maxSum/2) ? 'orange' : "blue")
                    }
                ],
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                },
            });
        }
        fetchMonths().then(() => {
                console.log("Data loaded sucessfully");
            }
        )

    }, []);
    const [chartData, setCharData] = useState(initData)

    return (
        <div className="App">
            <div className="header">
                <span id="text">Lecko test application</span>
            </div>
            <div className="container">
                <Bar data={chartData}></Bar>
            </div>
            <div className="container">
            </div>
        </div>
    )
}

export default App;
