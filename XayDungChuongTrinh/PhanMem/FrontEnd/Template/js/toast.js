
function showToast(message, type = 'success') {
    // Create container if not exists
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }

    // Create toast element
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    // Icon based on type
    let icon = 'info';
    if (type === 'success') icon = 'check_circle';
    if (type === 'error') icon = 'error';
    if (type === 'warning') icon = 'warning';

    toast.innerHTML = `
        <span class="material-symbols-outlined ${type === 'success' ? 'text-green-500' : type === 'error' ? 'text-red-500' : 'text-amber-500'}">
            ${icon}
        </span>
        <div class="flex flex-col">
            <span class="text-sm font-bold text-slate-800">${type.charAt(0).toUpperCase() + type.slice(1)}</span>
            <span class="text-xs text-slate-500">${message}</span>
        </div>
    `;

    container.appendChild(toast);

    // Auto remove
    setTimeout(() => {
        toast.style.animation = 'toastOut 0.3s ease-in forwards';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}
