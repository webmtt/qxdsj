const features = [
  {
    type: "Feature",
    properties: {
      adcode: 150100,
      name: "呼和浩特市",
      center: [111.670801, 40.818311],
      centroid: [111.50328, 40.597159],
      childrenNum: 9,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 0,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 150200,
      name: "包头市",
      center: [109.840405, 40.658168],
      centroid: [110.266038, 41.559669],
      childrenNum: 9,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 1,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 150300,
      name: "乌海市",
      center: [106.825563, 39.673734],
      centroid: [106.874373, 39.426964],
      childrenNum: 3,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 2,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 150400,
      name: "赤峰市",
      center: [118.956806, 42.275317],
      centroid: [118.878287, 43.239274],
      childrenNum: 12,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 3,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 150500,
      name: "通辽市",
      center: [122.263119, 43.617429],
      centroid: [121.569548, 43.834485],
      childrenNum: 8,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 4,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 150600,
      name: "鄂尔多斯市",
      center: [109.99029, 39.817179],
      centroid: [108.63741, 39.425314],
      childrenNum: 9,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 5,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 150700,
      name: "呼伦贝尔市",
      center: [119.758168, 49.215333],
      centroid: [120.906868, 49.635235],
      childrenNum: 14,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 6,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 150800,
      name: "巴彦淖尔市",
      center: [107.416959, 40.757402],
      centroid: [107.575749, 41.453305],
      childrenNum: 7,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 7,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 150900,
      name: "乌兰察布市",
      center: [113.114543, 41.034126],
      centroid: [112.443089, 41.696864],
      childrenNum: 11,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 8,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 152200,
      name: "兴安盟",
      center: [122.070317, 46.076268],
      centroid: [121.341355, 46.241397],
      childrenNum: 6,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 9,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 152500,
      name: "锡林郭勒盟",
      center: [116.090996, 43.944018],
      centroid: [115.51632, 44.232965],
      childrenNum: 12,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 10,
      acroutes: [100000, 150000]
    }
  },
  {
    type: "Feature",
    properties: {
      adcode: 152900,
      name: "阿拉善盟",
      center: [105.706422, 38.844814],
      centroid: [102.42556, 40.532392],
      childrenNum: 3,
      level: "city",
      parent: { adcode: 150000 },
      subFeatureIndex: 11,
      acroutes: [100000, 150000]
    }
  }
];
const NMG = [];
features.forEach(item => {
  NMG.push({
    name: item.properties.name,
    value: item.properties.center.push("20")
  });
  NMG[item.properties.name] = item.center;
});

export function geoCoordMapFN() {
  let data = {};
  features.forEach(item => {
    data[item.properties.name] = [item.properties.center[0], item.properties.center[1]]
  });
  return data;
}

export function geoNameFN() {
  let data = [];
  features.forEach(item => {
    data.push({
      name: item.properties.name,
      value: "0"
    });
  });
  return data;
}

// console.log('NMG=', NMG)

// export default NMG;
