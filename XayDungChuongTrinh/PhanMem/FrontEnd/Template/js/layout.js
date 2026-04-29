/**
 * layout.js - Shared Layout Renderer
 * Injects sidebar + header dynamically into every inner page
 */

function renderLayout(activePage) {
  const session = requireAuth();
  if (!session) return;

  const roleConfig = getRoleConfig(session.role);

  // ── Build sidebar menu HTML ──────────────────────────
  const menuItems = roleConfig.menu.map(item => {
    const isActive = item.page === activePage;
    return `
      <a href="${item.page}"
         class="nav-item flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-all duration-200
                ${isActive
                  ? 'bg-red-50 text-red-700 font-bold border-r-4 border-red-700'
                  : 'text-slate-600 hover:bg-slate-100 hover:text-red-700'}"
         data-page="${item.page}">
        <span class="material-symbols-outlined text-xl ${isActive ? '' : 'text-slate-400'}"
              style="font-variation-settings: 'FILL' ${isActive ? '1' : '0'}, 'wght' 400, 'GRAD' 0, 'opsz' 24;">${item.icon}</span>
        <span>${item.label}</span>
      </a>`;
  }).join('');

  // Role badge styling
  const badgeHtml = `<span class="inline-block px-2 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider ${roleConfig.badgeColor}">${roleConfig.label}</span>`;

  // Avatar initials
  const initials = session.name.split(' ').slice(-2).map(n => n[0]).join('').toUpperCase();

  const sidebarHTML = `
  <aside id="app-sidebar" class="w-64 h-screen bg-white border-r border-slate-200 flex flex-col sticky top-0 shrink-0 z-40">
    <!-- Logo -->
    <div class="px-5 py-4 flex items-center gap-3 border-b border-slate-100">
      <div class="w-10 h-10 bg-red-700 rounded-xl flex items-center justify-center shadow-md">
        <span class="material-symbols-outlined text-white text-2xl" style="font-variation-settings: 'FILL' 1, 'wght' 700;">bloodtype</span>
      </div>
      <div>
        <h1 class="font-black text-red-700 text-sm leading-tight">Hệ thống Hiến máu</h1>
        <p class="text-[10px] text-slate-500 uppercase font-bold tracking-wider">TP. Đà Nẵng</p>
      </div>
    </div>

    <!-- User profile block -->
    <div class="px-4 py-4 border-b border-slate-100">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-full flex items-center justify-center text-white font-black text-sm shrink-0"
             style="background: linear-gradient(135deg, #af101a, #d32f2f);">
          ${initials}
        </div>
        <div class="overflow-hidden">
          <p class="text-sm font-bold text-slate-800 truncate">${session.name}</p>
          ${badgeHtml}
        </div>
      </div>
    </div>

    <!-- Navigation -->
    <nav class="flex-1 px-3 py-4 space-y-1 overflow-y-auto">
      ${menuItems}
    </nav>

    <!-- Bottom actions -->
    <div class="px-3 py-3 border-t border-slate-100 space-y-1">
      <a href="#" onclick="logout(); return false;"
         class="flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium text-slate-500 hover:bg-red-50 hover:text-red-700 transition-all">
        <span class="material-symbols-outlined text-xl text-slate-400">logout</span>
        <span>Đăng xuất</span>
      </a>
    </div>
  </aside>`;

  // ── Build header HTML ────────────────────────────────
  const headerHTML = `
  <header id="app-header" class="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-8 sticky top-0 z-30 shrink-0">
    <div class="flex items-center gap-4 flex-1 max-w-md">
      <div class="relative w-full">
        <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-lg">search</span>
        <input id="global-search" type="text" placeholder="Tìm kiếm nhanh..."
               class="w-full h-10 pl-10 pr-4 bg-slate-50 border border-slate-200 rounded-full text-sm
                      focus:ring-2 focus:ring-red-200 focus:border-red-400 outline-none transition-all" />
      </div>
    </div>
    <div class="flex items-center gap-3">
      <!-- Notification bell -->
      <button id="btn-notifications" class="relative w-10 h-10 flex items-center justify-center rounded-full hover:bg-slate-100 transition-colors" title="Thông báo">
        <span class="material-symbols-outlined text-slate-500 text-xl">notifications</span>
        <span class="absolute top-2 right-2 w-2 h-2 bg-red-600 rounded-full border-2 border-white animate-pulse"></span>
      </button>
      <!-- Divider -->
      <div class="h-8 w-px bg-slate-200"></div>
      <!-- User info -->
      <div class="flex items-center gap-2">
        <div class="text-right">
          <p class="text-xs font-bold text-slate-800 leading-tight">${session.name}</p>
          <p class="text-[10px] text-slate-500 uppercase tracking-tight">${roleConfig.label}</p>
        </div>
        <div class="w-9 h-9 rounded-full flex items-center justify-center text-white text-xs font-black shrink-0"
             style="background: linear-gradient(135deg, #af101a, #d32f2f);">${initials}</div>
      </div>
      <!-- Logout shortcut -->
      <button onclick="logout()" class="w-9 h-9 flex items-center justify-center rounded-full hover:bg-red-50 hover:text-red-600 text-slate-400 transition-colors" title="Đăng xuất">
        <span class="material-symbols-outlined text-xl">logout</span>
      </button>
    </div>
  </header>`;

  // ── Inject into page ─────────────────────────────────
  const wrapper = document.getElementById('app-wrapper');
  if (wrapper) {
    wrapper.innerHTML = sidebarHTML + `<div class="flex-1 flex flex-col min-h-screen overflow-x-hidden">${headerHTML}<main id="page-content" class="flex-1 bg-gray-50"></main></div>`;
    // Move existing main content into page-content
    const existingContent = document.getElementById('main-content');
    const pageContent = document.getElementById('page-content');
    if (existingContent && pageContent) {
      pageContent.appendChild(existingContent);
    }
  } else {
    // Inject into body at beginning
    const body = document.body;
    const mainEl = document.getElementById('main-content') || document.querySelector('main[data-page]');

    const layoutDiv = document.createElement('div');
    layoutDiv.className = 'w-full min-h-screen flex';
    layoutDiv.innerHTML = sidebarHTML + `<div class="flex-1 flex flex-col min-h-screen overflow-x-hidden">${headerHTML}</div>`;

    if (mainEl) {
      const contentWrapper = layoutDiv.querySelector('.flex-1.flex.flex-col');
      contentWrapper.appendChild(mainEl);
    }
    body.insertBefore(layoutDiv, body.firstChild);
  }

  // ── Notification panel (simple dropdown) ─────────────
  setupNotifications();
}

function setupNotifications() {
  const bell = document.getElementById('btn-notifications');
  if (!bell) return;

  const panel = document.createElement('div');
  panel.id = 'notif-panel';
  panel.className = 'hidden absolute right-4 top-16 w-80 bg-white rounded-xl shadow-2xl border border-slate-200 z-50 overflow-hidden';
  panel.innerHTML = `
    <div class="px-5 py-4 border-b border-slate-100 flex justify-between items-center">
      <h3 class="font-bold text-slate-800 text-sm">Thông báo</h3>
      <span class="text-xs text-red-600 font-bold">3 mới</span>
    </div>
    <div class="divide-y divide-slate-50 max-h-72 overflow-y-auto">
      ${[
        { icon: 'warning', color: 'text-red-500', bg: 'bg-red-50', title: 'Cảnh báo tồn kho O-', time: '5 phút trước', msg: 'Nhóm máu O- xuống dưới ngưỡng an toàn (< 10 túi).' },
        { icon: 'check_circle', color: 'text-green-500', bg: 'bg-green-50', title: 'Xét nghiệm hoàn tất', time: '1 giờ trước', msg: '28 túi máu từ chiến dịch Hè 2025 đã xét nghiệm âm tính.' },
        { icon: 'person_add', color: 'text-blue-500', bg: 'bg-blue-50', title: 'TNV mới đăng ký', time: '2 giờ trước', msg: '12 tình nguyện viên mới đăng ký chiến dịch Hải Châu.' },
      ].map(n => `
        <div class="flex gap-3 px-4 py-3 hover:bg-slate-50 cursor-pointer transition-colors">
          <div class="w-8 h-8 rounded-full ${n.bg} flex items-center justify-center shrink-0 mt-0.5">
            <span class="material-symbols-outlined text-sm ${n.color}" style="font-variation-settings:'FILL' 1;">${n.icon}</span>
          </div>
          <div>
            <p class="text-xs font-bold text-slate-800">${n.title}</p>
            <p class="text-[11px] text-slate-500 mt-0.5">${n.msg}</p>
            <p class="text-[10px] text-slate-400 mt-1">${n.time}</p>
          </div>
        </div>`).join('')}
    </div>
    <div class="px-5 py-3 border-t border-slate-100 text-center">
      <a href="#" class="text-xs font-bold text-red-600 hover:underline">Xem tất cả thông báo</a>
    </div>`;

  document.body.appendChild(panel);

  bell.addEventListener('click', (e) => {
    e.stopPropagation();
    panel.classList.toggle('hidden');
  });
  document.addEventListener('click', () => panel.classList.add('hidden'));
}
