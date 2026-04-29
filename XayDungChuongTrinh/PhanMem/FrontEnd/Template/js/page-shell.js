/**
 * page-shell.js
 * Reusable sidebar + header renderer for all inner pages.
 * Usage: call initPage('PageFilename.html') at DOMContentLoaded.
 */

function initPage(activePage) {
  const session = requireAuth();
  if (!session) return;

  const rc = getRoleConfig(session.role);
  const initials = session.name.split(' ').slice(-2).map(n => n[0]).join('').toUpperCase();
  const badge = `<span class="inline-block px-2 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider ${rc.badgeColor}">${rc.label}</span>`;

  const menuHTML = rc.menu.map(item => {
    const active = item.page === activePage;
    return `<a href="${item.page}"
      class="flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium transition-all duration-200 cursor-pointer
      ${active ? 'bg-red-50 text-red-700 font-bold' : 'text-slate-600 hover:bg-slate-100 hover:text-red-700'}">
      <span class="material-symbols-outlined text-xl"
            style="font-variation-settings:'FILL' ${active?1:0},'wght' 400,'GRAD' 0,'opsz' 24;">${item.icon}</span>
      <span>${item.label}</span>
    </a>`;
  }).join('');

  const avatarGradient = 'background:linear-gradient(135deg,#af101a,#d32f2f)';

  const sidebarHTML = `
  <aside class="w-64 min-h-screen bg-white border-r border-slate-200 flex flex-col sticky top-0 shrink-0 z-40">
    <div class="px-5 py-4 flex items-center gap-3 border-b border-slate-100">
      <div class="w-10 h-10 bg-red-700 rounded-xl flex items-center justify-center shadow-md">
        <span class="material-symbols-outlined text-white text-2xl" style="font-variation-settings:'FILL' 1,'wght' 700;">bloodtype</span>
      </div>
      <div>
        <h1 class="font-black text-red-700 text-sm leading-tight">Hệ thống Hiến máu</h1>
        <p class="text-[10px] text-slate-500 uppercase font-bold tracking-wider">TP. Đà Nẵng</p>
      </div>
    </div>
    <div class="px-4 py-4 border-b border-slate-100">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-full flex items-center justify-center text-white font-black text-sm shrink-0"
             style="${avatarGradient}">${initials}</div>
        <div class="overflow-hidden">
          <p class="text-sm font-bold text-slate-800 truncate">${session.name}</p>
          ${badge}
        </div>
      </div>
    </div>
    <nav class="flex-1 px-3 py-4 space-y-1 overflow-y-auto">${menuHTML}</nav>
    <div class="px-3 py-3 border-t border-slate-100">
      <button onclick="logout()"
        class="w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium text-slate-500 hover:bg-red-50 hover:text-red-700 transition-all">
        <span class="material-symbols-outlined text-xl text-slate-400">logout</span>Đăng xuất
      </button>
    </div>
  </aside>`;

  const headerHTML = `
  <header class="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-8 sticky top-0 z-30 shrink-0">
    <div class="flex items-center gap-4 flex-1 max-w-md">
      <div class="relative w-full">
        <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-lg">search</span>
        <input type="text" placeholder="Tìm kiếm nhanh..."
               class="w-full h-10 pl-10 pr-4 bg-slate-50 border border-slate-200 rounded-full text-sm
                      focus:ring-2 focus:ring-red-200 focus:border-red-400 outline-none transition-all"/>
      </div>
    </div>
    <div class="flex items-center gap-3">
      <button id="bell-btn" class="relative w-10 h-10 flex items-center justify-center rounded-full hover:bg-slate-100 transition-colors">
        <span class="material-symbols-outlined text-slate-500 text-xl">notifications</span>
        <span class="absolute top-2 right-2 w-2 h-2 bg-red-600 rounded-full border-2 border-white animate-pulse"></span>
      </button>
      <div class="h-8 w-px bg-slate-200"></div>
      <div class="flex items-center gap-2">
        <div class="text-right">
          <p class="text-xs font-bold text-slate-800 leading-tight">${session.name}</p>
          <p class="text-[10px] text-slate-500 uppercase tracking-tight">${rc.label}</p>
        </div>
        <div class="w-9 h-9 rounded-full flex items-center justify-center text-white text-xs font-black shrink-0"
             style="${avatarGradient}">${initials}</div>
      </div>
      <button onclick="logout()"
        class="w-9 h-9 flex items-center justify-center rounded-full hover:bg-red-50 hover:text-red-600 text-slate-400 transition-colors" title="Đăng xuất">
        <span class="material-symbols-outlined text-xl">logout</span>
      </button>
    </div>
  </header>`;

  // Mount
  const sidebarMount = document.getElementById('sidebar-mount');
  const headerMount  = document.getElementById('header-mount');
  if (sidebarMount) sidebarMount.innerHTML = sidebarHTML;
  if (headerMount)  headerMount.innerHTML  = headerHTML;

  // Simple bell notification
  setTimeout(() => {
    const bell = document.getElementById('bell-btn');
    if (!bell) return;
    bell.addEventListener('click', (e) => {
      e.stopPropagation();
      const existing = document.getElementById('notif-dropdown');
      if (existing) { existing.remove(); return; }
      const drop = document.createElement('div');
      drop.id = 'notif-dropdown';
      drop.className = 'fixed right-6 top-20 w-80 bg-white rounded-2xl shadow-2xl border border-slate-200 z-50 overflow-hidden';
      drop.innerHTML = `
        <div class="px-5 py-4 border-b border-slate-100 flex justify-between items-center">
          <h3 class="font-bold text-slate-800 text-sm">Thông báo</h3>
          <span class="text-xs text-red-600 font-bold">3 chưa đọc</span>
        </div>
        <div class="divide-y divide-slate-50">
          <div class="flex gap-3 px-4 py-3 hover:bg-slate-50 cursor-pointer">
            <div class="w-8 h-8 rounded-full bg-red-50 flex items-center justify-center shrink-0">
              <span class="material-symbols-outlined text-red-500 text-sm" style="font-variation-settings:'FILL' 1;">warning</span>
            </div>
            <div>
              <p class="text-xs font-bold text-slate-800">Cảnh báo tồn kho O-</p>
              <p class="text-[11px] text-slate-500 mt-0.5">Nhóm máu O- xuống dưới ngưỡng an toàn</p>
              <p class="text-[10px] text-slate-400 mt-1">5 phút trước</p>
            </div>
          </div>
          <div class="flex gap-3 px-4 py-3 hover:bg-slate-50 cursor-pointer">
            <div class="w-8 h-8 rounded-full bg-green-50 flex items-center justify-center shrink-0">
              <span class="material-symbols-outlined text-green-500 text-sm" style="font-variation-settings:'FILL' 1;">check_circle</span>
            </div>
            <div>
              <p class="text-xs font-bold text-slate-800">Xét nghiệm hoàn tất</p>
              <p class="text-[11px] text-slate-500 mt-0.5">28 túi máu từ chiến dịch Hè 2025</p>
              <p class="text-[10px] text-slate-400 mt-1">1 giờ trước</p>
            </div>
          </div>
          <div class="flex gap-3 px-4 py-3 hover:bg-slate-50 cursor-pointer">
            <div class="w-8 h-8 rounded-full bg-blue-50 flex items-center justify-center shrink-0">
              <span class="material-symbols-outlined text-blue-500 text-sm" style="font-variation-settings:'FILL' 1;">person_add</span>
            </div>
            <div>
              <p class="text-xs font-bold text-slate-800">12 TNV mới đăng ký</p>
              <p class="text-[11px] text-slate-500 mt-0.5">Chiến dịch Hải Châu tháng 5</p>
              <p class="text-[10px] text-slate-400 mt-1">2 giờ trước</p>
            </div>
          </div>
        </div>
        <div class="px-5 py-3 border-t border-slate-100 text-center">
          <a href="#" class="text-xs font-bold text-red-600 hover:underline">Xem tất cả</a>
        </div>`;
      document.body.appendChild(drop);
      setTimeout(() => document.addEventListener('click', () => drop.remove(), { once: true }), 50);
    });
  }, 200);
}

// Utility: show toast notification
function showToast(msg, type='success') {
  const colors = { success: 'bg-emerald-600', error: 'bg-red-600', info: 'bg-blue-600', warning: 'bg-amber-500' };
  const icons  = { success: 'check_circle', error: 'error', info: 'info', warning: 'warning' };
  const toast = document.createElement('div');
  toast.className = `fixed bottom-8 left-1/2 -translate-x-1/2 flex items-center gap-3 px-5 py-3.5 ${colors[type]} text-white rounded-2xl shadow-2xl z-[200] text-sm font-semibold transition-all`;
  toast.style.animation = 'slideUp 0.3s ease';
  toast.innerHTML = `<span class="material-symbols-outlined text-xl" style="font-variation-settings:'FILL' 1;">${icons[type]}</span>${msg}`;
  document.body.appendChild(toast);
  setTimeout(() => toast.remove(), 3000);
}

// Modal helpers
function openModal(id) { document.getElementById(id)?.classList.remove('hidden'); }
function closeModal(id) { document.getElementById(id)?.classList.add('hidden'); }
