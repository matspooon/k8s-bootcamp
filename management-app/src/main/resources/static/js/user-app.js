const { createApp } = Vue;

createApp({
  data() {
    return {
      newUser: { username: '', email: '', role: '', active: true },
      grid: null,
    };
  },
  mounted() {
    this.loadGrid();
  },
  methods: {
    async loadGrid() {
      const response = await fetch('/users/data');
      const data = await response.json();

      if (this.grid) this.grid.updateConfig({ data }).forceRender();
      else {
        this.grid = new gridjs.Grid({
          columns: [
            { name: "ID", id: "id" },
            { name: "Username", id: "username" },
            { name: "Email", id: "email" },
            { name: "Role", id: "role" },
            {
              name: "Active", id: "active",
              formatter: cell => cell ? '✅' : '❌'
            },
            {
              name: "Actions",
              formatter: (_, row) => {
                const id = row.cells[0].data;
                return gridjs.h('button', {
                  className: 'delete-btn',
                  onClick: () => this.deleteUser(id)
                }, 'Delete');
              }
            }
          ],
          data: data,
          pagination: { enabled: true, limit: 10 },
          sort: true,
          search: true,
        }).render(document.getElementById("userGrid"));
      }
    },

    async addUser() {
      await fetch('/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.newUser),
      });
      this.newUser = { username: '', email: '', role: '', active: true };
      this.loadGrid();
    },

    async deleteUser(id) {
      if (!confirm('Delete this user?')) return;
      await fetch(`/users/${id}`, { method: 'DELETE' });
      this.loadGrid();
    },
  }
}).mount("#app");
