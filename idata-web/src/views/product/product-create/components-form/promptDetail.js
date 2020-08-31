const DefaulePromptData = {
  types: [],
  units: [],
  products: []
};
class PromptDetailData {
  constructor(option) {
  }
  setPromptData(t, u, p) {
    DefaulePromptData.types = t;
    DefaulePromptData.units = u;
    DefaulePromptData.products = p;
  }
  getPromptData() {
    return DefaulePromptData
  }
}
export default PromptDetailData;
