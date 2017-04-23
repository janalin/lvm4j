var heatdiffusion = (function () {

    return {
        heat: function (graph) {

            var t =
                [
                    [0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0],
                    [0.0002, 0.0035, 0.0035, 0.0002, 0.0002, 0.0002, 0.0002, 0.0002, 0.0002, 0.0675, 0.0152, 0.0053, 0.0053, 0.0053, 0.085, 0.085, 0.085, 0.085, 0.085, 0.4039, 0.1393, 0.0091, 0.008, 0.007, 0.0278, 0.1428, 0.0091, 0.4039, 0.1462, 0.0053, 0.0056, 0.0032, 0.0032, 0.0032, 0.0032, 0.0032, 0.0006, 0.0007, 0.0009, 0.001, 0.0049, 0.3958, 0.0342, 0.0001, 0.0025, 0.0026, 0.0009, 0.0025, 0.0003, 0.0003, 0.0008, 0.0044, 0.0004, 0.0003, 0.0025, 0.0003, 0.0002, 0.0003, 0.0003, 0.0003, 0.0019, 0.0003, 0.0022, 0.0053, 0.0762, 0],
                    [0.0041, 0.0151, 0.0151, 0.0041, 0.0041, 0.0041, 0.0041, 0.0041, 0.0041, 0.0793, 0.0346, 0.0143, 0.0143, 0.0143, 0.1073, 0.1073, 0.1073, 0.1073, 0.1073, 0.1106, 0.1255, 0.0375, 0.0371, 0.0388, 0.0611, 0.0675, 0.0374, 0.0602, 0.0657, 0.0143, 0.0225, 0.0235, 0.0235, 0.0235, 0.0235, 0.0235, 0.011, 0.0098, 0.0158, 0.0153, 0.0245, 0.0467, 0.0421, 0.0039, 0.019, 0.02, 0.0126, 0.0189, 0.0075, 0.0057, 0.0139, 0.0286, 0.0081, 0.0103, 0.0172, 0.0094, 0.0078, 0.0094, 0.0104, 0.0094, 0.013, 0.0094, 0.0162, 0.0143, 0.1023, 0.002],
                    [0.0041, 0.0151, 0.0151, 0.0041, 0.0041, 0.0041, 0.0041, 0.0041, 0.0041, 0.0793, 0.0346, 0.0143, 0.0143, 0.0143, 0.1073, 0.1073, 0.1073, 0.1073, 0.1073, 0.1106, 0.1255, 0.0375, 0.0371, 0.0388, 0.0611, 0.0675, 0.0374, 0.0602, 0.0657, 0.0143, 0.0225, 0.0235, 0.0235, 0.0235, 0.0235, 0.0235, 0.011, 0.0098, 0.0158, 0.0153, 0.0245, 0.0467, 0.0421, 0.0039, 0.019, 0.02, 0.0126, 0.0189, 0.0075, 0.0057, 0.0139, 0.0286, 0.0081, 0.0103, 0.0172, 0.0094, 0.0078, 0.0094, 0.0104, 0.0094, 0.013, 0.0094, 0.0162, 0.0143, 0.1023, 0.002]
                ];

            function dragstarted(d) {
                if (!d3.event.active) simulation.alphaTarget(0.3).restart();
                d.fx = d.x;
                d.fy = d.y;
            }

            function dragged(d) {
                d.fx = d3.event.x;
                d.fy = d3.event.y;
            }

            function dragended(d) {
                if (!d3.event.active) simulation.alphaTarget(0);
                d.fx = null;
                d.fy = null;
            }

            function draw(graph) {
                var svg = d3.select("#network"),
                    width = +svg.attr("width"),
                    height = +svg.attr("height");

                var color = d3.scaleLinear().domain([0, 1]).range(["#8e0152", "#c51b7d", "#de77ae", "#f1b6da", "#fde0ef", "#f7f7f7", "#e6f5d0", "#b8e186", "#7fbc41", "#4d9221", "#276419"]);

                var simulation = d3.forceSimulation()
                    .force("link", d3.forceLink().id(function (d) {
                        return d.id;
                    }))
                    .force("charge", d3.forceManyBody())
                    .force("center", d3.forceCenter(width / 2, height / 2));

                svg.selectAll("g").data([]).exit().remove();

                var link = svg.append("g")
                    .attr("class", "links")
                    .selectAll("line")
                    .data(graph.links)
                    .enter().append("line")
                    .attr("stroke-width", function (d) {
                        return 1;
                    });

                var node = svg.append("g")
                    .attr("class", "nodes")
                    .selectAll("circle")
                    .data(graph.nodes)
                    .enter().append("circle")
                    .attr("r", 5)
                    .style("stroke", "black")
                    .style("stroke-width", "2px")
                    .attr("fill", function (d) {
                        if (idx === 3)
                            return d3.interpolateOranges(.25);
                        return d3.interpolateOranges(1 - (1 - 5 * d.heat));
                    })
                    .call(d3.drag()
                        .on("start", dragstarted)
                        .on("drag", dragged)
                        .on("end", dragended));

                simulation.nodes(graph.nodes).on("tick", ticked);

                simulation.force("link").links(graph.links);

                function ticked() {
                    link
                        .attr("x1", function (d) {
                            return d.source.x;
                        })
                        .attr("y1", function (d) {
                            return d.source.y;
                        })
                        .attr("x2", function (d) {
                            return d.target.x;
                        })
                        .attr("y2", function (d) {
                            return d.target.y;
                        });

                    node
                        .attr("cx", function (d) {
                            return d.x;
                        })
                        .attr("cy", function (d) {
                            return d.y;
                        });
                }
            }

            for (var i in graph.nodes) {
                graph.nodes[i].heat = t[0][i];
            }
            draw(graph);
            var idx = 1;
            d3.interval(function () {
                for (var i in graph.nodes) {
                    graph.nodes[i].heat = t[idx][i];
                }
                draw(graph, idx);
                idx = idx + 1;
                if (idx >= 4) idx = 0;
            }, 5500);
        }
    };
})();

(function () {
    $.getJSON("./json/dat.json", heatdiffusion.heat);
})();

