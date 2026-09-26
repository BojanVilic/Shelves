# Shelves — Android Architecture Lab

## Purpose

Shelves is a small Android book-discovery app used as a working baseline for architecture investigation. The product surface is intentionally simple so the interesting work stays on structure, ownership, and trade-offs rather than feature scope.

Open Library is the remote data source. The app can browse a few subjects, search works, and open a book details screen.

This repository is a lab, not a prescription. The baseline below is conventional on purpose. Each architecture topic names a real problem that shows up once a product grows. The exercises ask you to investigate those problems against this codebase. They do not tell you which design to choose.

## Current baseline

Single `:app` module. Conventional MVVM:

- Retrofit calls Open Library
- Nullable DTOs decode JSON with unknown keys ignored
- Defensive mappers produce domain models
- One repository implementation is injected into screen ViewModels
- Compose screens collect `StateFlow` UI state
- Type-safe Navigation Compose routes for Discover, Search, and Book Details

Screens:

- **Discover** — horizontal rows for Science Fiction, Fantasy, and Classics
- **Search** — one query, one page of results
- **Book Details** — cover, title, authors, year when present, description when present

Deliberate limitations left in place for the lab: no extra Gradle modules, no use-case layer, no shared `Result` type, no base ViewModel, no navigation wrapper, no shared book-shelf component, no cache or Room, no design system, no shared error model.

## Architecture topics

These are problem spaces, not recommended solutions.

### Modularization

When does a single module become expensive to build, own, or reason about? What are the costs of splitting too early—API surface, dependency cycles, slower local iteration—and how do you recognize a boundary that is real rather than aspirational?

### Contracts

What does one feature owe another? How do you keep feature teams from reaching into each other's internals while still sharing enough model and navigation information to compose a product?

### Parent / child composition

Who owns shared chrome (bottom bar, top bar, auth gate)? How do child screens request navigation, result delivery, or shared actions without becoming coupled to a specific parent implementation?

### State

Where does UI state live, how long does it survive configuration changes, and what happens when the same book appears on Discover, Search, and Details at once? How do you avoid duplicate sources of truth without inventing a global store by default?

### Navigation

How should destinations, arguments, and back stacks be expressed so deep links, process death, and multi-entry flows stay coherent? What breaks when routes are strings, and what breaks when every screen is over-typed?

### Dependency injection

What is worth constructing in a graph versus constructing near the call site? How do you keep the graph from becoming a second architecture that every feature must learn?

### Data

How do remote DTOs, domain models, and UI models stay honest about missing fields, partial payloads, and evolving APIs? Where should mapping happen so screens do not silently depend on transport shape?

### Caching

When is a network round-trip enough, and when do offline, stale-while-revalidate, or local persistence become necessary? What is the cost of introducing a cache before you know the consistency rules?

### Errors

How should failures surface to users and to logs when Discover, Search, and Details each fail differently? What is lost when every screen invents its own string, and what is lost when errors are forced through one shared type too early?

### Concurrency

How do parallel subject loads, author lookups, and search cancellation interact with ViewModel scope? Where do races, duplicate requests, and abandoned work show up in a real session?

### Lifecycle

What should keep running when the user leaves a screen, rotates the device, or backgrounds the app? How do you keep collectors, image loads, and repository calls aligned with Android lifecycle without leaking?

### Design system

When do duplicated rows and one-off Material pieces become a maintenance tax? What is the cost of extracting a shared shelf before the product language is stable?

### Compose API and performance

Which recompositions are accidental? How do lists, images, and nested scroll containers behave under real data sizes, and what APIs make that cost visible?

### Accessibility

Can every primary action be reached and understood with TalkBack, larger text, and sufficient contrast? Where do custom rows and image-only covers hide content from assistive technology?

### Observability

How do you know which screen failed, which endpoint was slow, and whether a mapping bug reached production? What telemetry is useful before you have a large user base?

### Feature flags

How do you ship incomplete architecture changes behind a flag without forking the whole navigation graph or data layer?

### Testing

What is worth a JVM unit test, what needs Compose UI tests, and what only shows up in an end-to-end walk on a device? How do fakes at the repository boundary help or hide integration risk?

### Gradle

How do version catalogs, plugin application, and build times change as dependencies and modules grow? What build complexity is accidental?

### CI

What should block a merge for a lab app that still talks to a live third-party API? How do flaky network and device tests get isolated from deterministic unit checks?

### Static analysis

Which lint, formatting, and API checks catch architecture drift early, and which ones become noise that the team ignores?

### API stability

Open Library payloads vary: string vs object descriptions, missing years, negative cover ids, author keys without names. How do you keep the app stable when the remote contract is only partially documented?

### ADRs

When is a written Architecture Decision Record worth the time, and what belongs in one so future readers understand the trade-off instead of only the chosen pattern?

## Principles

- Prefer a boring working app over premature abstraction.
- Duplicate until the duplication hurts in a measurable way.
- Keep the product flow readable: API → DTO → mapper → repository → ViewModel → UI.
- Leave architectural questions open in this README; answer them in code and ADRs only after investigating the baseline.
- Do not invent modules, frameworks, or wrappers to “look ready” for scale the app does not have yet.

## Exercises

High-level investigations against this baseline. No implementation steps are provided here.

1. **Module boundaries** — Identify candidate seams in `:app` and argue whether any are worth extracting now, later, or never.
2. **Feature contracts** — Sketch what Discover, Search, and Details would need to expose if they lived in separate owners.
3. **Shared UI pressure** — Compare the duplicated book rows and decide what evidence would justify a shared component.
4. **State ownership** — Trace book identity across screens and propose how overlapping state should be owned.
5. **Navigation growth** — Extend the mental model to nested tabs, deep links, and returning a result to Search without rewriting the host.
6. **Error strategy** — Catalog failure modes from the live API and sort them into user-visible, retryable, and log-only buckets.
7. **Caching policy** — Define when Discover sections or Details should be remembered, refreshed, or discarded.
8. **Concurrency audit** — Review parallel subject and author fetches for cancellation, partial failure, and ordering.
9. **Test pyramid** — Decide which new behaviors belong in mapper tests, ViewModel tests, Compose tests, and device walks.
10. **ADR practice** — Write one ADR for a real trade-off you would change in this baseline, including the rejected options.

## License / notes

Open Library data is used for educational architecture work. Respect their API guidance and identify the client with a clear User-Agent.
