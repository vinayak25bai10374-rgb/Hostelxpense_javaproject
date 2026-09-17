# Problem Statement — HostelXpense

## Problem Statement
Students living in shared hostel rooms or flats frequently split recurring
costs — food deliveries, wifi recharges, electricity bills, cleaning
services — among a group. In practice this is usually tracked informally on
paper, in a group chat, or in someone's memory, which leads to confusion,
forgotten dues, and unequal contributions. There is no simple, dedicated tool
for a small group of students to log shared expenses, automatically compute
who owes whom, and keep an eye on personal spending against a monthly
budget.

## Scope of the Project
HostelXpense is a single-user-operated (shared-terminal), console-based Java
application intended for a small hostel room or flat group (typically 2–6
students). It covers:
- Registering the students who will be sharing expenses.
- Logging shared expenses and splitting them equally among chosen
  participants.
- Tracking each student's personal monthly budget and running balance.
- Providing simple analytics (category-wise spend, who-owes-most ranking,
  overall summary).

It does **not** cover: real online payments/settlement, multi-user
concurrent access, unequal/weighted bill splitting, or a graphical/web
interface — these are noted as future enhancements.

## Target Users
- Hostel roommates or flatmates who jointly pay for shared essentials.
- Small student groups (e.g. a mess table, a project team ordering
  equipment) who want a lightweight way to track shared costs without
  spreadsheets.

## High-Level Features
1. **Student Management** — add and list hostel members with auto-generated
   IDs and room numbers.
2. **Shared Expense Logging & Automatic Bill Splitting** — record who paid,
   for what, how much, and who shares it; the system computes equal shares
   and updates each participant's balance instantly.
3. **Budget Tracking & Alerts** — each student sets a personal monthly
   budget; the system flags when they're near or over that limit.
4. **Analytics & Reporting** — text-based spend-by-category chart, an
   owed-amount ranking, and an overall hostel spending summary.
5. **Persistent, File-Based Storage** — all records survive across sessions
   using plain CSV files, with an activity log for basic monitoring.
