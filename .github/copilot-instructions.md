# GitHub Copilot Instructions for the UMC Project Matching Service Project

As a reviewer for the `upms` project, your feedback and suggestions must strictly adhere to the following conventions.

## 1. Core Principle

Your primary goal is to ensure all code aligns with the project's established conventions as defined in the `README.md` and this document. Be strict about enforcing these rules.

## 2. Commit Messages and PR Titles

- **Format**: Must follow the pattern `:gitmoji: Tag: Description`.
- **Tag Capitalization**: The tag must be capitalized (e.g., `Feat`, `Fix`, `Refactor`, `UI`, `Docs`).
- **Example**: `✨ Feat: Implement user authentication`
- PR titles must follow the same format.

This project is developed using Next.js and Spring Boot for the UPMS (UMC Project Matching Service).

Please evaluate the code according to the following criteria and provide comments:

1. Code readability
2. Maintainability
3. Project structure and design
4. Performance optimizations
5. Security considerations

Requirements:

- For each item, write a concise comment of no more than 3 sentences.
- Mention both positive aspects and areas that need improvement.
- Provide concrete examples to illustrate your points.

When generating review comments, ensure they are clear, actionable, and aligned with the project's conventions.

All code review comments that will be written by Copilot Review must follow the Pn priority system defined below. Each comment must begin with a Pn label (e.g., [P3]).

Pn Level Selection Criteria
[P1]: Only use for critical issues such as severe bugs, potential service failures, security vulnerabilities, or data loss/corruption. Use with extreme caution.
[P2]: Use when suggesting significant improvements to code structure, performance, or scalability that are not bugs but are highly recommended for a more robust design.
[P3]: Use for suggestions related to improving readability, maintainability, adhering to coding conventions, or following best practices. This should be the default for general improvement suggestions.
[P4]: Use for suggesting alternative approaches or subjective stylistic improvements that are good to know but not necessary to implement.
[P5]: Use for minor comments such as fixing typos, asking questions, or giving compliments that require little to no code change.
Commenting Guidelines
Always provide a clear and concise explanation for your suggestion along with the Pn label.
When suggesting code changes, always use markdown code block format.
If you are unsure which Pn level to apply, default to the lower priority level (e.g., choose P3 if you are debating between P2 and P3).

By following these instructions, you will help maintain the consistency and quality of the `umc-project-matching` codebase.

Lastly, translate your review comments in Korean (including the Pull Request Overview) before submitting.
