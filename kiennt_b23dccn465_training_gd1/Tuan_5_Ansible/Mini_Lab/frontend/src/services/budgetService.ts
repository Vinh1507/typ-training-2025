import api from "../api";

// 🟢 Lấy tất cả ngân sách
export const getBudgets = async () => {
  try {
    const res = await api.get("/budgets");
    return res.data || [];
  } catch (error) {
    console.error("❌ Lỗi khi lấy danh sách ngân sách:", error);
    return [];
  }
};

// 🟢 Lấy ngân sách theo ví
export const getBudgetsByWallet = async (walletId: number) => {
  try {
    const res = await api.get(`/budgets/wallet/${walletId}`);
    return res.data || [];
  } catch (error) {
    console.error("❌ Lỗi khi lấy ngân sách theo ví:", error);
    return [];
  }
};

// 🟢 Tạo ngân sách mới
export const createBudget = async (budget: any) => {
  try {
    const res = await api.post("/budgets", budget);
    return res.data;
  } catch (error) {
    console.error("❌ Lỗi khi tạo ngân sách:", error);
    throw error;
  }
};

// 🟢 Cập nhật chi tiêu (tăng/giảm)
export const updateSpent = async (budgetId: number, delta: number) => {
  try {
    const res = await api.put(`/budgets/${budgetId}/spent?delta=${delta}`);
    return res.data;
  } catch (error) {
    console.error("❌ Lỗi khi cập nhật chi tiêu:", error);
    throw error;
  }
};

// 🟢 Xóa ngân sách
export const deleteBudget = async (id: number) => {
  try {
    await api.delete(`/budgets/${id}`);
  } catch (error) {
    console.error("❌ Lỗi khi xóa ngân sách:", error);
    throw error;
  }
};
