<p align="center">
  <a href="https://srsyduts.com">
    <img src="./docs/banner.svg" alt="pancreas3000Banner" width="800">
  </a>
</p>
#
A closed-loop insulin pump controller hosted on a Raspberry Pi, and controlled by your phone over a hotspot. 

> ⚠️ **Disclaimer:** This is a bench-top educational project only. It has not been tested or certified for clinical use, and must
> never be used to dose insulin in anybody ⚠️

## Overview

Insulin pumps are super expensive, and for good reason. They have zero margin for error. 
But when the nukes drop (and they will), what do you do if all you have is a raspberry Pi and a stepper motor? 
Is it possible to continue your needle-free lifestyle?

Costing roughly $190, pancreas 3000 does what insulin pumps have been doing since the early 2000's.

An open-loop insulin pump is cable of running a basal program, while offering meal boluses and correction dose from a bg reading.

In other words, you tell it what your current blood glucose is, and how much you are eating, and the pump does the rest for you. 

## Features

- 1hz control loop
- Basal-bolus logic for dose calculation, 0.05ml increments
- Two phase bolus delivery: preview pumps suggested dose, then confirm
- Insulin-on-board tracking from therapy log
- Safety limits: max dose caps, rate limiting, fault detection
- Embedded HTTP server hosted on the user's hotspot lets dashboard be accessible from phone browser

## Architecture

## Parts 
## Tech Stack

- Language: Java 
- Platform: Raspberry Pi (Debian)
- Key libraries/tools: JavaHttp

### Build & Run
Nobody should actually ever try to recreate this. This pump would probably end up killing you. Safety-critical code is **NOT** written in Java.
